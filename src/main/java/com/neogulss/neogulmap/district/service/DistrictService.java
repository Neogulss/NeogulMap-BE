package com.neogulss.neogulmap.district.service;


import com.neogulss.neogulmap.district.dto.DistrictDTO;
import com.neogulss.neogulmap.district.dto.DistrictDTO.DistrictRecommendList;
import com.neogulss.neogulmap.district.mapper.DistrictMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DistrictService {

  private final DistrictMapper districtMapper;

  @Transactional
  public DistrictDTO.Response getDistrictRecommendList(DistrictDTO.Request request){
    DistrictDTO.Response response = DistrictDTO.Response.builder().build();

    List<DistrictRecommendList> districtRecommendList = districtMapper.getDistrictRecommendList(request);

    response.setDistrictRecommendLists(districtRecommendList);

    return response;
  }
}
