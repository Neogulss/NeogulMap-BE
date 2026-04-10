package com.neogulss.neogulmap.category.service;

import com.neogulss.neogulmap.category.dto.CategoryDTO;
import com.neogulss.neogulmap.category.mapper.CategoryMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 업종 카테고리 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryMapper categoryMapper;

    /**
     * 대분류 코드에 해당하는 업종 목록 조회
     *
     * @param request CategoryDTO.Request
     * @return CategoryDTO.Response
     */
    @Transactional
    public CategoryDTO.Response getCategoryList(CategoryDTO.Request request) {

        log.info("[getCategoryList] 업종 목록 조회 - mainCategoryCode: [{}]", request.getMainCategoryCode());

        List<CategoryDTO.CategoryItem> categoryList = categoryMapper.getCategoryList(request);

        return CategoryDTO.Response.builder()
            .categoryList(categoryList)
            .build();
    }
}
