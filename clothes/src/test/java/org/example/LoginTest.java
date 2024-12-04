package org.example;

import data.DataLogin;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.Excel;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static org.testng.Assert.assertEquals;

public class LoginTest {
    String URL = "http://localhost:8080/sessions/login";
    private WebDriver driver;
    private final String path = Excel.DataPath + "dataLogin.xlsx";
    private Set<DataLogin> loginResults;
    private DataLogin loginData;

    @BeforeClass
    public void setUpTestData() throws IOException {
        loginResults = new LinkedHashSet<>();
    }

    @BeforeMethod
    public void initializeDriver() {
        driver = new ChromeDriver();
        driver.get(URL);
        loginData = new DataLogin();
    }

    @Test(dataProvider = "loginData")
    private void performLogInTest(String email, String password, String expected) throws InterruptedException {
        executeSigin(email, password);
        String currentUrl = driver.getCurrentUrl();
        loginData.setEmail(email);
        loginData.setPassword(password);
        loginData.setTime(new Date());
        loginData.setExpectedResult(expected);
        loginData.setActualResult(currentUrl);
        Thread.sleep(2000);
        assertEquals(currentUrl, expected);
    }

    private void executeSigin(String email, String password) {
        driver.findElement(By.name("username")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.id("loginButton")).click();
    }

    @AfterMethod
    public void captureTestResult(ITestResult result) throws IOException {
        String resultStatus = (result.getStatus() == ITestResult.SUCCESS) ? "success" :
                (result.getStatus() == ITestResult.FAILURE) ? "fail" :
                        (result.getStatus() == ITestResult.SKIP) ? "SKIP" : "UNKNOWN";

        loginData.setTestResult(resultStatus);
        if (result.getStatus() == ITestResult.FAILURE) {
            String customMessage = "Test method " + result.getMethod().getMethodName() +
                    " failed with error: " + result.getThrowable().getMessage();
            loginData.setErrorMessage(customMessage);
            String path = Excel.ImagePath + UUID.randomUUID() + ".jpg";
            Excel.captureScreenshot(driver, path);
            loginData.setScreenshotPath(new File(path).getAbsolutePath());
        }
        loginResults.add(loginData);
        driver.quit();
    }

    @AfterClass
    public void writeTestLogs() throws IOException {
        Excel.writeLog(path, "Result", loginResults);
    }

    @DataProvider(name = "loginData")
    public Object[][] Login() throws IOException {
        XSSFWorkbook workbook = Excel.getWorkbook(path);
        XSSFSheet sheet = Excel.getSheet(workbook, "Data");
        return Excel.readSheetData(sheet);
    }
}