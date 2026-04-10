package com.neogulss.neogulmap.category.controller;

import com.neogulss.neogulmap.category.dto.CategoryDTO;
import com.neogulss.neogulmap.category.service.CategoryService;
import com.neogulss.neogulmap.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 업종 카테고리 Controller
 */
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 대분류 코드에 해당하는 업종 목록 조회
     *
     * @param request CategoryDTO.Request
     * @return ResponseEntity.ok(response)
     */
    @PostMapping("/list")
    public ResponseEntity<BaseResponse<Object>> getCategoryList(
        @RequestBody CategoryDTO.Request request) {
        BaseResponse<Object> response = BaseResponse.builder()
            .data(categoryService.getCategoryList(request))
            .build();
        return ResponseEntity.ok(response);
    }
}
