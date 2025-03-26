package com.springboot.category.mapper;

import com.springboot.category.dto.CategoryDto;
import com.springboot.category.entity.Category;
import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto.ResponseDto categoryToCategoryResponseDto(Category category);
    List<CategoryDto.ResponseDto> categoryToCategoryResponseDtos(List<Category> categories);
}
