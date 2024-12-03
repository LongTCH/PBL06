package utils;

import data.TestReportData;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.util.Set;

public class Excel {
    public static String DataPath = "src/test/resources/data/";
    public static String ImagePath = "src/test/resources/images/";

    public static XSSFWorkbook getWorkbook(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream file_input = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(file_input);
        file_input.close();
        return workbook;
    }

    public static XSSFSheet getSheet(XSSFWorkbook workbook, String sheetName) {
        return workbook.getSheet(sheetName);
    }

    public static CellStyle getRowStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return style;
    }

    public static String readCellValue(XSSFSheet sheet, int row, int column) {
        XSSFCell cell = sheet.getRow(row).getCell(column);
        if (cell == null) {
            return "";
        }
        try {
            return switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue();
                case NUMERIC -> String.format("%.0f", cell.getNumericCellValue());
                case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
                default -> "";
            };
        } catch (Exception e) {
            return "";
        }
    }

    public static void captureScreenshot(WebDriver driver, String path) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File(path);
        org.apache.commons.io.FileUtils.copyFile(screenshot, destFile);
    }

    public static Object[][] readSheetData(XSSFSheet sheet) {
        int rows = sheet.getPhysicalNumberOfRows();
        int cols = sheet.getRow(0).getLastCellNum();
        Object[][] data = new Object[rows - 1][cols];
        for (int row = 1; row < rows; row++) {
            XSSFRow currentRow = sheet.getRow(row);
            if (currentRow == null) {
                continue;
            }
            for (int col = 0; col < cols; col++) {
                data[row - 1][col] = readCellValue(sheet, row, col);
            }
        }
        return data;
    }

    public static void export(XSSFWorkbook workbook, String filePath) throws IOException {
        FileOutputStream output = new FileOutputStream(filePath);
        workbook.write(output);
        output.close();
    }

    public static <T extends TestReportData> void writeLog(String src, String sheetName, Set<T> logs) throws IOException{
        XSSFWorkbook workbook = Excel.getWorkbook(src);
        XSSFSheet sheet = Excel.getSheet(workbook, sheetName);
        int firstRow = 0, lastRow = sheet.getPhysicalNumberOfRows();
        if (lastRow < firstRow) {
            lastRow = firstRow;
        }
        CellStyle rowStyle = Excel.getRowStyle(workbook);
        for (T log : logs) {
            Row row = sheet.createRow(lastRow++);
            row.setHeightInPoints(100);
            row.setRowStyle(rowStyle);
            log.writeDataToRow(row, sheet);
        }
        export(workbook, src);
    }
}