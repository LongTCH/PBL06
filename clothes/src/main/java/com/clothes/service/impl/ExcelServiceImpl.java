package com.clothes.service.impl;

import com.clothes.dto.ExcelReader;
import com.clothes.service.ExcelService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Override
    public <T extends ExcelReader<T>> List<T> readerExcelFile(MultipartFile file, Class<T> clazz) throws Exception {
        List<T> result = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum() - 1; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            result.add(clazz.getDeclaredConstructor().newInstance().fromRow(row));
        }
        workbook.close();
        return result;
    }
}