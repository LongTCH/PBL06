package data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.IOException;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class TestReportData {
    private String expectedResult;
    private String actualResult;
    private String testResult;
    private String errorMessage = null;
    private String screenshotPath = null;
    private Date time;

    public void writeTestData(int startIndex, Row row, XSSFSheet sheet) throws IOException {
        CreationHelper creationHelper = sheet.getWorkbook().getCreationHelper();
        CellStyle style = row.getRowStyle();
        Cell cell;
        cell = row.createCell(startIndex);
        cell.setCellValue(getExpectedResult());
        cell.setCellStyle(style);

        cell = row.createCell(startIndex + 1);
        cell.setCellValue(getActualResult());
        cell.setCellStyle(style);

        cell = row.createCell(startIndex + 2);
        cell.setCellValue(getTestResult());
        cell.setCellStyle(style);

        if (getErrorMessage() != null) {
            cell = row.createCell(startIndex + 3);
            cell.setCellValue(getErrorMessage());
            cell.setCellStyle(style);
        }

        if (getScreenshotPath() != null) {
            cell = row.createCell(startIndex + 4);
            cell.setCellValue("Image");
            cell.setCellStyle(style);
            XSSFHyperlink hyperlink = (XSSFHyperlink) creationHelper.createHyperlink(HyperlinkType.URL);
            hyperlink.setAddress(getScreenshotPath().replace("\\", "/"));
            cell.setHyperlink(hyperlink);
        }
        cell = row.createCell(startIndex + 5);
        cell.setCellValue(getTime());
        CellStyle datetimeStyle = sheet.getWorkbook().createCellStyle();
        datetimeStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
        cell.setCellStyle(datetimeStyle);
    }

    public abstract void writeDataToRow(Row row, XSSFSheet sheet) throws IOException;
}