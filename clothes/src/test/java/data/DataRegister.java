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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataRegister extends TestReportData {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Override
    public void writeDataToRow(Row row, XSSFSheet sheet) throws IOException {
        CellStyle style = row.getRowStyle();
        Cell cell;
        cell = row.createCell(0);
        cell.setCellValue(getFirstName());
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(getLastName());
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue(getEmail());
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue(getPassword());
        cell.setCellStyle(style);
        writeTestData(4, row, sheet);
    }
}