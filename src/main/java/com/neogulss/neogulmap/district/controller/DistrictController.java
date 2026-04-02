package com.neogulss.neogulmap.district.controller;

import com.neogulss.neogulmap.common.response.BaseResponse;
import com.neogulss.neogulmap.district.dto.DistrictDTO;
import com.neogulss.neogulmap.district.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DistrictController {

  private final DistrictService districtService;

  @PostMapping("/district")
  public ResponseEntity<BaseResponse<Object>> getDistrictRecommendList(
      @RequestBody DistrictDTO.Request request){
    BaseResponse<Object> response = BaseResponse.builder().data(districtService.getDistrictRecommendList(request)).build();
    return ResponseEntity.ok(response);
  }
}
