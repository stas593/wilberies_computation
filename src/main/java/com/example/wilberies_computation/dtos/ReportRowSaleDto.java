package com.example.wilberies_computation.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportRowSaleDto {

  private String number;
  private String vendorСode;
  private String name;
  private LocalDate orderDate;
  private LocalDate sellDate;
  private BigDecimal sellingPrice;
  private BigDecimal tax;
  private BigDecimal toMoneyTransfer;

}
