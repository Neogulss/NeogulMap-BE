package com.neogulss.neogulmap.category.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * 업종 카테고리 DTO
 */
public class CategoryDTO {

    /**
     * 업종 카테고리 목록 조회 Request
     */
    @Alias("CategoryRequest")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Request {
        /** 대분류 카테고리 코드 (MC1: 외식업, MC2: 서비스업, MC3: 소매업) */
        private String mainCategoryCode;
    }

    /**
     * 업종 카테고리 목록 조회 Response
     */
    @Alias("CategoryResponse")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Response {
        /** 업종 목록 */
        private List<CategoryItem> categoryList;
    }

    /**
     * 업종 항목
     */
    @Alias("CategoryItem")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class CategoryItem {
        /** 업종 코드 (예: CS100001) */
        private String serviceIndustryCode;
        /** 업종명 (예: 한식음식점) */
        private String serviceIndustryCodeName;
    }
}
