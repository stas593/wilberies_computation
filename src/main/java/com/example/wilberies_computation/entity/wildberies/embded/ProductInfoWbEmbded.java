package com.example.wilberies_computation.entity.wildberies.embded;

import com.example.wilberies_computation.entity.wildberies.ProductEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoWbEmbded {

  @Column(name = "name")
  private String name;
  @ManyToOne
  @JoinColumn(name = "vendor_code")
  private ProductEntity product;
  @Column(name = "how_many_sold")
  private long howManySold = 0;
  @Column(name = "delivery_cost_total")
  private BigDecimal deliveryCostTotal = BigDecimal.ZERO;
  @Column(name = "accrued")
  private BigDecimal accrued = BigDecimal.ZERO;
  @Column(name = "tax")
  private BigDecimal tax = BigDecimal.ZERO;
  @Column(name = "accrued_with_tax")
  private BigDecimal accruedWitchTax;
  @Column(name = "net_profit")
  private BigDecimal NetProfit;

}
