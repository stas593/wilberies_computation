package com.example.wilberies_computation.controllers;

import com.example.wilberies_computation.dtos.ReportComputeResultDto;
import com.example.wilberies_computation.services.Computation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/wb-computation")
public class ExcelComputationController {

  @Autowired
  private Computation computation;

  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ReportComputeResultDto computeAndSave(@RequestPart MultipartFile xlsx) {
    return computation.computeAndSave(xlsx);
  }

}
