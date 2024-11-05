package com.clothes.service.impl;

import com.clothes.dto.ExcelReader;
import com.clothes.service.CategoriesService;
import com.clothes.service.ExcelService;
import com.clothes.service.GroupsService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {
    private final CategoriesServiceImpl categoriesServiceImpl;
    private final GroupsServiceImpl groupsServiceImpl;

    public ExcelServiceImpl(CategoriesServiceImpl categoriesServiceImpl, GroupsServiceImpl groupsServiceImpl) {
        this.categoriesServiceImpl = categoriesServiceImpl;
        this.groupsServiceImpl = groupsServiceImpl;
    }

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
            Constructor<T> constructor = clazz.getConstructor(CategoriesService.class, GroupsService.class);
            T instance = constructor.newInstance(categoriesServiceImpl, groupsServiceImpl);
            result.add(instance.fromRow(row));
        }
        workbook.close();
        return result;
    }
}