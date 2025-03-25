package com.springboot.category.controller;

import com.springboot.category.dto.CategoryDto;
import com.springboot.category.entity.Category;
import com.springboot.category.entity.SubCategory;
import com.springboot.category.mapper.CategoryMapper;
import com.springboot.category.service.CategoryService;
import com.springboot.comment.entity.Comment;
import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryMapper mapper;
    private final CategoryService categoryService;

    public CategoryController(CategoryMapper mapper, CategoryService categoryService) {
        this.mapper = mapper;
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 목록 전체 조회", description = "회원가입시 조회될 카테고리 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "카테고리 목록 조회 완료"),
            @ApiResponse(responseCode = "404", description = "category not found"),
    })
    @GetMapping
    public ResponseEntity getCategories() {
        List<Category> categories = categoryService.findCategories();
        List<CategoryDto.ResponseDto> result = mapper.categoryToCategoryResponseDtos(categories);
        return new ResponseEntity(new SingleResponseDto<>(result), HttpStatus.OK);
    }
}
