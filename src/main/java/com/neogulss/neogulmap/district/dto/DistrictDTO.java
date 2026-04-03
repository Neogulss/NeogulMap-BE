package com.neogulss.neogulmap.district.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

public class DistrictDTO {

  @Alias("DistrictResponse")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class Response {
    private List<DistrictRecommendList> districtRecommendLists;
  }

  @Alias("DistrictRequest")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class Request {
    private String mainCategoryCode;
    private String serviceIndustryCodeName;
  }

  @Alias("DistrictRecommendList")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class DistrictRecommendList{
    private int adminDongCode;
    private String adminDongName;
    private int districtCode;
    private String districtName;
    private double longitude;
    private double latitude;
    private String serviceIndustryCode;
    private String serviceIndustryCodeName;
  }
}
