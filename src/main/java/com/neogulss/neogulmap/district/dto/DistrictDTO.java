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
    private String initial_capital;
    private String detail_category_name;
  }

  @Alias("DistrictRecommendList")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class DistrictRecommendList{
    private int districtCode;
    private String districtName;
    private int initialCapital;
    private double xCoordinate;
    private double yCcoordinate;
    private int area;
    private int storeCount;
  }
}
