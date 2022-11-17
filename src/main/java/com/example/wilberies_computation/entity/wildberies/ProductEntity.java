package com.example.wilberies_computation.entity.wildberies;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {


  @Id
  @Column(name = "vendor_code", nullable = false)
  private String vendorCode;
  @Column(name = "full_name")
  private String fullName;
  @Column(name = "total_sold")
  private Long totalSold = 0L;
  @Column(name = "prime_cost")
  private BigDecimal primeCost  = BigDecimal.ZERO;
  @Column(name = "marketplace")
  @Enumerated(EnumType.STRING)
  private MarketplacesEnum marketplace;


}
