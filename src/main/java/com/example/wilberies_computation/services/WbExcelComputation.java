package com.example.wilberies_computation.services;

import com.example.wilberies_computation.dtos.ReportComputeResultDto;
import com.example.wilberies_computation.dtos.ReportRowDeliveryDto;
import com.example.wilberies_computation.dtos.ReportRowSaleDto;
import com.example.wilberies_computation.entity.ReportEntity;
import com.example.wilberies_computation.entity.embded.ProductInfoEmbded;
import com.example.wilberies_computation.repositories.ReportRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WbExcelComputation implements Computation {

  @Autowired
  private ReportRepository reportRepository;

  @SneakyThrows
  public ReportComputeResultDto computeAndSave(MultipartFile xlsx) {
    Workbook workbook = new XSSFWorkbook(xlsx.getInputStream());
    Sheet sheet = workbook.getSheetAt(0);
    List<ReportRowSaleDto> rowsSaleList = excelSheetToListSell(sheet);
    List<ReportRowDeliveryDto> rowsDeliveryList = excelSheetToListDelivery(sheet);
    this.createReport(rowsSaleList, rowsDeliveryList);
    rowsSaleList.forEach(System.out::println);
    rowsDeliveryList.forEach(System.out::println);
    return ReportComputeResultDto.builder().build();

  }

  private List<ReportRowSaleDto> excelSheetToListSell(Sheet sheet) {
    DataFormatter formatter = new DataFormatter();
    List<ReportRowSaleDto> rowsList = new ArrayList<>();
    for (Row row : sheet) {
      if (row.getRowNum() == 0) {
        continue;
      }
      if (formatter.formatCellValue(row.getCell(10)).equals("Продажа")) {
        rowsList.add(ReportRowSaleDto.builder()
            .number(formatter.formatCellValue(row.getCell(0)))
            .vendorСode(formatter.formatCellValue(row.getCell(5)))
            .name(formatter.formatCellValue(row.getCell(6)))
            .orderDate(LocalDate.parse(formatter.formatCellValue(row.getCell(11))))
            .sellDate(LocalDate.parse(formatter.formatCellValue(row.getCell(12))))
            .sellingPrice(BigDecimal.valueOf(row.getCell(15).getNumericCellValue()))
            .tax(BigDecimal.valueOf(row.getCell(15).getNumericCellValue())
                .multiply(BigDecimal.valueOf(0.07d)))
            .toMoneyTransfer(BigDecimal.valueOf(row.getCell(29).getNumericCellValue()))
            .build());
      }
    }
    return rowsList;
  }

  private List<ReportRowDeliveryDto> excelSheetToListDelivery(Sheet sheet) {
    DataFormatter formatter = new DataFormatter();
    List<ReportRowDeliveryDto> rowsList = new ArrayList<>();
    for (Row row : sheet) {
      if (row.getRowNum() == 0) {
        continue;
      }
      if (formatter.formatCellValue(row.getCell(10)).equals("Логистика")) {
        rowsList.add(ReportRowDeliveryDto.builder()
            .number(formatter.formatCellValue(row.getCell(0)))
            .vendorСode(formatter.formatCellValue(row.getCell(5)))
            .name(formatter.formatCellValue(row.getCell(6)))
            .orderDate(LocalDate.parse(formatter.formatCellValue(row.getCell(11))))
            .toMoneyPaid(BigDecimal.valueOf(row.getCell(32).getNumericCellValue()))
            .build());
      }
    }
    return rowsList;
  }

  private ReportEntity createReport(List<ReportRowSaleDto> saleRows,
      List<ReportRowDeliveryDto> deliveryRows) {
    BigDecimal howPaid = saleRows.stream().map(ReportRowSaleDto::getToMoneyTransfer)
        .reduce((BigDecimal::add)).get();
    BigDecimal deliveryPaid = deliveryRows.stream().map(ReportRowDeliveryDto::getToMoneyPaid)
        .reduce((BigDecimal::add)).get();
    List<ProductInfoEmbded> productInfoEmbdeds = saleRowsToProductInfoEmbded(saleRows);

    ReportEntity reportEntity = ReportEntity.builder()
        .howPaid(howPaid)
        .deliveryPaid(deliveryPaid)
        .revenue(howPaid.subtract(deliveryPaid))
        .quantitySold((int) saleRows.stream().count())
        .build();

    return reportRepository.save(reportEntity);
  }

  private List<ProductInfoEmbded> saleRowsToProductInfoEmbded(List<ReportRowSaleDto> saleRows) {
    List<ProductInfoEmbded> productInfoEmbdeds = new ArrayList<>();
    Set<String> vendorCodes = Set.copyOf(
        saleRows.stream().map(ReportRowSaleDto::getVendorСode).collect(
            Collectors.toList()));
    vendorCodes.forEach(System.out::println);

    vendorCodes.stream()
        .map(code -> saleRows.stream()
            .filter(row -> row.getVendorСode().equals(code))
            .map(row -> productInfoEmbdeds.add(ProductInfoEmbded.builder()
                    .vendorCode(row.getVendorСode())
                    .
                .build()))

        )

    return Collections.emptyList();
  }
}

