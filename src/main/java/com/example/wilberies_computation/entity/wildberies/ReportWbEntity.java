package com.example.wilberies_computation.entity.wildberies;

import com.example.wilberies_computation.entity.wildberies.embded.ProductInfoWbEmbded;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
@Table(name = "reports_wb")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportWbEntity {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "date_from", unique=true)
  private LocalDate dateFrom;
  @Column(name = "date_to", unique=true)
  private LocalDate dateTo;

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
  List<ProductInfoWbEmbded> productInfos;

}
