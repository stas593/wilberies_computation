package com.example.wilberies_computation.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportRowDeliveryDto {

  private String number;
  private String vendorСode;
  private String name;
  private LocalDate orderDate;
  private BigDecimal toMoneyPaid;

}
