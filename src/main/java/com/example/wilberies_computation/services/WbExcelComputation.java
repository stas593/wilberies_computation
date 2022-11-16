package com.example.wilberies_computation.services;

import com.example.wilberies_computation.dtos.wilberies.ReportComputeResultDto;
import com.example.wilberies_computation.dtos.wilberies.ReportRowDeliveryDto;
import com.example.wilberies_computation.dtos.wilberies.ReportRowSaleDto;
import com.example.wilberies_computation.entity.wildberies.ProductEntity;
import com.example.wilberies_computation.entity.wildberies.ReportWbEntity;
import com.example.wilberies_computation.entity.wildberies.embded.ProductInfoWbEmbded;
import com.example.wilberies_computation.repositories.ProductRepository;
import com.example.wilberies_computation.repositories.ReportRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
  @Autowired
  private ProductRepository productRepository;

  @SneakyThrows
  public ReportComputeResultDto computeAndSave(MultipartFile xlsx) {
    Workbook workbook = new XSSFWorkbook(xlsx.getInputStream());
    Sheet sheet = workbook.getSheetAt(0);
    List<ReportRowSaleDto> rowsSaleList = excelSheetToListSell(sheet);
    List<ReportRowDeliveryDto> rowsDeliveryList = excelSheetToListDelivery(sheet);
    this.createReport(rowsSaleList, rowsDeliveryList);
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
            .saleDate(LocalDate.parse(formatter.formatCellValue(row.getCell(12))))
            .toMoneyPaid(BigDecimal.valueOf(row.getCell(32).getNumericCellValue()))
            .build());
      }
    }
    return rowsList;
  }

  private ReportWbEntity createReport(List<ReportRowSaleDto> saleRows,
      List<ReportRowDeliveryDto> deliveryRows) {
    BigDecimal howPaid = saleRows.stream().map(ReportRowSaleDto::getToMoneyTransfer)
        .reduce((BigDecimal::add)).get();
    BigDecimal deliveryPaid = deliveryRows.stream().map(ReportRowDeliveryDto::getToMoneyPaid)
        .reduce((BigDecimal::add)).get();
    List<ProductInfoWbEmbded> productInfoEmbdeds = saleRowsToProductInfoEmbded(saleRows);
    productInfoEmbdeds.stream().forEach(System.out::println);
    ReportWbEntity reportEntity = ReportWbEntity.builder()
        .howPaid(howPaid)
        .dateFrom(saleRows.stream().map(ReportRowSaleDto::getSellDate)
            .min(Comparator.comparing(LocalDate::toEpochDay)).get())
        .dateTo(saleRows.stream().map(ReportRowSaleDto::getSellDate)
            .max(Comparator.comparing(LocalDate::toEpochDay)).get())
        .deliveryPaid(deliveryPaid)
        .revenue(howPaid.subtract(deliveryPaid))
        .quantitySold((int) saleRows.stream().count())
        .productInfos(productInfoEmbdeds)
        .build();

    return reportRepository.save(reportEntity);
  }

  private List<ProductInfoWbEmbded> saleRowsToProductInfoEmbded(List<ReportRowSaleDto> saleRows) {
    List<ProductInfoWbEmbded> productInfoEmbdeds = new ArrayList<>();
    Set<String> vendorCodes = Set.copyOf(
        saleRows.stream().map(ReportRowSaleDto::getVendorСode).collect(
            Collectors.toList()));

    for (String vendorCode : vendorCodes) {
      ProductInfoWbEmbded infoEmbded = new ProductInfoWbEmbded();
      ProductEntity product = productRepository.getByVendorCode(vendorCode).orElse(null);
      for (ReportRowSaleDto rowSale : saleRows) {
        if (rowSale.getVendorСode().equals(vendorCode)) {
          if (infoEmbded.getName() == null) {
            infoEmbded.setName(rowSale.getName());
          }
          if (infoEmbded.getProduct() == null) {
            infoEmbded.setProduct(product);
          }
          infoEmbded.setHowManySold(infoEmbded.getHowManySold() + 1L);
          infoEmbded.setAccrued(infoEmbded.getAccrued().add(rowSale.getToMoneyTransfer()));
          infoEmbded.setTax(infoEmbded.getTax().add(rowSale.getTax()));
          infoEmbded.setAccruedWitchTax(infoEmbded.getAccrued().subtract(infoEmbded.getTax()));
          infoEmbded.setNetProfit(infoEmbded.getAccruedWitchTax()
              .subtract(BigDecimal.valueOf(infoEmbded.getHowManySold()).multiply(
                  product == null ? BigDecimal.ONE
                      : product.getCostPrice() == null ? BigDecimal.valueOf(0)
                          : product.getCostPrice())));
        }
      }
      productInfoEmbdeds.add(infoEmbded);
    }

    return productInfoEmbdeds;
  }
}

