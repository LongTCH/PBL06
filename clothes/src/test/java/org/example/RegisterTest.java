package org.example;

import data.DataRegister;
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
import java.util.*;

import static org.testng.Assert.assertEquals;

public class RegisterTest {
    String URL = "http://localhost:8080/sessions/register";
    private WebDriver driver;
    private final String path = Excel.DataPath + "dataRegister.xlsx";
    private Set<DataRegister> registerResults;
    private DataRegister registerData;

    @BeforeClass
    public void setUpTestData() {
        registerResults = new LinkedHashSet<>();
    }

    @BeforeMethod
    public void initializeDriver() {
        driver = new ChromeDriver();
        driver.get(URL);
        registerData = new DataRegister();
    }

    @Test(dataProvider = "signupData")
    private void performSignUpTest(String firstName, String lastName, String email, String password, String expected) throws InterruptedException {
        executeSignup(firstName, lastName, email, password);
        String currentUrl = driver.getCurrentUrl();
        registerData.setFirstName(firstName);
        registerData.setLastName(lastName);
        registerData.setEmail(email);
        registerData.setPassword(password);
        registerData.setTime(new Date());
        registerData.setExpectedResult(expected);
        registerData.setActualResult(currentUrl);
        Thread.sleep(2000);
        assertEquals(currentUrl, expected);
    }

    private void executeSignup(String firstName, String lastName, String email, String password) {
        driver.get(URL);
        driver.findElement(By.name("firstName")).sendKeys(firstName);
        driver.findElement(By.name("lastName")).sendKeys(lastName);
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.id("register")).click();
    }

    @AfterMethod
    public void captureTestResult(ITestResult result) throws IOException {
        String resultStatus = (result.getStatus() == ITestResult.SUCCESS) ? "success" :
                (result.getStatus() == ITestResult.FAILURE) ? "fail" :
                        (result.getStatus() == ITestResult.SKIP) ? "SKIP" : "UNKNOWN";

        registerData.setTestResult(resultStatus);
        if (result.getStatus() == ITestResult.FAILURE) {
            String customMessage = "Test method " + result.getMethod().getMethodName() +
                    " failed with error: " + result.getThrowable().getMessage();
            registerData.setErrorMessage(customMessage);
            String path = Excel.ImagePath + UUID.randomUUID() + ".jpg";
            Excel.captureScreenshot(driver, path);
            registerData.setScreenshotPath(new File(path).getAbsolutePath());
        }
        registerResults.add(registerData);
        driver.quit();
    }

    @AfterClass
    public void writeTestLogs() throws IOException {
        Excel.writeLog(path, "Result", registerResults);
    }

    @DataProvider(name = "signupData")
    public Object[][] Signup() throws IOException {
        XSSFWorkbook workbook = Excel.getWorkbook(path);
        XSSFSheet sheet = Excel.getSheet(workbook, "Data");

        Object[][] allData = Excel.readSheetData(sheet);

        List<Object[]> validData = new ArrayList<>();
        for (Object[] row : allData) {
            boolean isValidRow = Arrays.stream(row).anyMatch(cell -> cell != null && !cell.toString().trim().isEmpty());
            if (isValidRow) {
                validData.add(row);
            }
        }

        return validData.toArray(new Object[0][0]);
    }

}
