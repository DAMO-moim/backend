package com.springboot.category.service;

import com.springboot.category.entity.Category;
import com.springboot.category.repository.CategoryRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findCategories(){
        return categoryRepository.findAll();
    }

    public Category findVerifiedCategory(long categoryId){
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category category = optionalCategory.orElseThrow(()->
                new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));

        return category;
    }
}
