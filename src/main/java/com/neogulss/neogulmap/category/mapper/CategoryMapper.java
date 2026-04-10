package com.neogulss.neogulmap.category.mapper;

import com.neogulss.neogulmap.category.dto.CategoryDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 업종 카테고리 Mapper
 */
@Mapper
public interface CategoryMapper {

    /**
     * 대분류 코드에 해당하는 업종 목록 조회
     *
     * @param request CategoryDTO.Request
     * @return List CategoryDTO.CategoryItem
     */
    List<CategoryDTO.CategoryItem> getCategoryList(CategoryDTO.Request request);
}
