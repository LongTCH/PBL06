package com.clothes.controller.admin;

import com.clothes.model.Order;
import com.clothes.service.StatisticsService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller("adminStatisticsController")
@RequestMapping("/admin/revenue")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public String index(Model model) {
        Page<Order> completedOrders = Page.empty();
        long totalRevenue = 0;
        model.addAttribute("completedOrders", completedOrders);
        model.addAttribute("current_page", "statistic_active");
        model.addAttribute("totalRevenue", totalRevenue);
        return "admin/statistics/revenue";
    }

    @GetMapping("/ajax/daily")
    public String loadDateRevenueData(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(required = false) String date,
                                      Model model) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Order> completedOrders = Page.empty();
        long totalRevenue = 0;

        if (date != null && !date.isEmpty()) {
            LocalDate selectedDate = LocalDate.parse(date);
            completedOrders = statisticsService.findCompletedOrdersByDate(selectedDate, pageable);
            totalRevenue = statisticsService.calculateRevenueByDate(selectedDate);
        }
        model.addAttribute("completedOrders", completedOrders);
        model.addAttribute("totalRevenue", totalRevenue);
        return "admin/statistics/revenue :: dailyTableSection";
    }

    @GetMapping("/ajax/monthly")
    public String loadMonthlyRevenueData(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(required = false) String month,
                                         Model model) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Order> completedOrders = Page.empty();
        long totalRevenue = 0;
        if (month != null && !month.isEmpty()) {
            YearMonth selectedMonth = YearMonth.parse(month);
            completedOrders = statisticsService.findCompletedOrdersByMonth(selectedMonth, pageable);
            totalRevenue = statisticsService.calculateRevenueByMonth(selectedMonth);
        }

        model.addAttribute("completedOrders", completedOrders);
        model.addAttribute("totalRevenue", totalRevenue);
        return "admin/statistics/revenue :: monthlyTableSection";
    }

    @GetMapping("/export/excel_daily")
    @ResponseBody
    public ResponseEntity<byte[]> exportDailyExcel(@RequestParam String date, @RequestParam List<String> attributes) {
        LocalDate selectedDate = LocalDate.parse(date);
        List<Order> completedOrders = statisticsService.findCompletedOrdersByDate(selectedDate);
        String fileName = "doanh_thu_" + selectedDate + ".xlsx";
        return exportExcel(completedOrders, attributes, fileName);
    }

    @GetMapping("/export_month/excel")
    @ResponseBody
    public ResponseEntity<byte[]> exportMonthlyExcel(@RequestParam String month, @RequestParam List<String> attributes) {
        YearMonth selectedMonth = YearMonth.parse(month);
        List<Order> completedOrders = statisticsService.findCompletedOrdersByMonth(selectedMonth);
        String fileName = "doanh_thu_" + selectedMonth + ".xlsx";
        return exportExcel(completedOrders, attributes, fileName);
    }

    private ResponseEntity<byte[]> exportExcel(List<Order> orders, List<String> attributes, String fileName) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Doanh Thu");
            Row headerRow = sheet.createRow(0);
            int cellIndex = 0;

            if (attributes.contains("orderId")) {
                headerRow.createCell(cellIndex++).setCellValue("Mã đơn hàng");
            }
            if (attributes.contains("customerName")) {
                headerRow.createCell(cellIndex++).setCellValue("Tên khách hàng");
            }
            if (attributes.contains("orderDate")) {
                headerRow.createCell(cellIndex++).setCellValue("Ngày đặt hàng");
            }
            if (attributes.contains("amount")) {
                headerRow.createCell(cellIndex++).setCellValue("Tổng tiền");
            }
            if (attributes.contains("status")) {
                headerRow.createCell(cellIndex++).setCellValue("Trạng thái");
            }

            int rowNum = 1;
            for (Order order : orders) {
                Row row = sheet.createRow(rowNum++);
                cellIndex = 0;
                if (attributes.contains("orderId")) {
                    String orderId = "ORDER-" + String.valueOf(order.getId()).substring(Math.max(String.valueOf(order.getId()).length() - 5, 0));
                    row.createCell(cellIndex++).setCellValue(orderId);
                }
                if (attributes.contains("customerName")) {
                    row.createCell(cellIndex++).setCellValue(order.getCustomerName());
                }
                if (attributes.contains("orderDate")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
                    String formattedDate = order.getCreatedDate().format(formatter);
                    row.createCell(cellIndex++).setCellValue(formattedDate);
                }
                if (attributes.contains("amount")) {
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    String formattedAmount = decimalFormat.format(order.getAmount()) + " đ";
                    row.createCell(cellIndex++).setCellValue(formattedAmount);
                }
                if (attributes.contains("status")) {
                    row.createCell(cellIndex++).setCellValue(order.getStatus().getVietnameseStatus());
                }
            }
            workbook.write(bos);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + fileName);
            return new ResponseEntity<>(bos.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
