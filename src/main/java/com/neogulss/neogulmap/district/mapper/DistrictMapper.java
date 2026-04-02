package com.neogulss.neogulmap.district.mapper;

import com.neogulss.neogulmap.district.dto.DistrictDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistrictMapper {
  List<DistrictDTO.DistrictRecommendList> getDistrictRecommendList(DistrictDTO.Request request);
}
