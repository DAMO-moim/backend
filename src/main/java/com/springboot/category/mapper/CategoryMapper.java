package com.springboot.category.mapper;

import com.springboot.category.dto.CategoryDto;
import com.springboot.category.dto.SubCategoryDto;
import com.springboot.category.entity.Category;
import com.springboot.category.entity.SubCategory;
import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto.ResponseDto categoryToCategoryResponseDto(Category category);
    List<CategoryDto.ResponseDto> categoryToCategoryResponseDtos(List<Category> categories);

    default List<SubCategoryDto.Response> subCategoriesToResponses(List<SubCategory> subCategories) {
        return subCategories.stream()
                .map(sc -> new SubCategoryDto.Response(sc.getSubCategoryId(), sc.getSubCategoryName()))
                .collect(Collectors.toList());
    }
}
