package data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataLogin extends TestReportData {
    private String email;
    private String password;

    @Override
    public void writeDataToRow(Row row, XSSFSheet sheet) throws IOException {
        CellStyle style = row.getRowStyle();
        Cell cell;
        cell = row.createCell(0);
        cell.setCellValue(getEmail());
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(getPassword());
        cell.setCellStyle(style);
        writeTestData(2, row, sheet);
    }
}