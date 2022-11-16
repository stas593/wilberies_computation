package com.example.wilberies_computation.entity;

import com.example.wilberies_computation.dtos.ProductInfoDto;
import com.example.wilberies_computation.entity.embded.ProductInfoEmbded;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Data
@Table(name = "reports")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportEntity {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "how_paid")
  private BigDecimal howPaid;
  @Column(name = "quantity_sold")
  private int quantitySold;
  @Column(name = "delivery_paid")
  private BigDecimal deliveryPaid;
  @Column(name = "revenue")
  private BigDecimal revenue;

  @LazyCollection(LazyCollectionOption.TRUE)
  @ElementCollection
  @CollectionTable(
      name = "product_infos",
      joinColumns = @JoinColumn(name = "reports_id")
  )
  List<ProductInfoEmbded> productInfos;

}
