package com.example.wilberies_computation.dtos.wilberies;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportRowFineDto {

  private String number;
  private String vendorСode;
  private String name;
  private LocalDate orderDate;
  private LocalDate saleDate;
  private BigDecimal finesPaid;

}
