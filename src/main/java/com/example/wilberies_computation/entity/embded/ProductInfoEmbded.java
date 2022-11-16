package com.example.wilberies_computation.entity.embded;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;

@Data
@Embeddable
@Builder
public class ProductInfoEmbded {

  @Column(name = "name")
  private String name;
  @Column(name = "vendor_code")
  private String vendorCode;
  @Column(name = "cost_price")
  private String costPrice;
  @Column(name = "selling_price")
  private BigDecimal sellingPrice;
  @Column(name = "how_many_sold")
  private long howManySold;
  @Column(name = "delivery_cost")
  private BigDecimal deliveryCost;
  @Column(name = "delivery_cost_total")
  private BigDecimal deliveryCostTotal;
  @Column(name = "accrued")
  private BigDecimal accrued;
  @Column(name = "accrued_with_tax")
  private BigDecimal accruedWitchTax;

}
