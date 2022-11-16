package com.example.wilberies_computation.services;

import com.example.wilberies_computation.dtos.ReportComputeResultDto;
import org.springframework.web.multipart.MultipartFile;

public interface Computation {

  ReportComputeResultDto computeAndSave(MultipartFile xlsx);

}
