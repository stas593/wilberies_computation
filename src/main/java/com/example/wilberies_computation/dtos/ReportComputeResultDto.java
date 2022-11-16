package com.example.wilberies_computation.dtos;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ReportComputeResultDto {

  private BigDecimal howPaid;
  private int quantitySold;
  private int deliveryPaid;
  List<ProductInfoDto> productInfos;



}
