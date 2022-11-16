package com.example.wilberies_computation.dtos.wilberies;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInfoDto {

  private String name;
  private String vendorCode;
  private String costPrice;
  private BigDecimal sellingPrice;
  private long howManySold;
  private BigDecimal deliveryCost;
  private BigDecimal deliveryCostTotal;
  private BigDecimal accrued;

}
