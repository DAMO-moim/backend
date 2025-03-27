package com.springboot.category.service;

import com.springboot.category.entity.Category;
import com.springboot.category.entity.SubCategory;
import com.springboot.category.repository.CategoryRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
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

//    public void findVerifiedCategoryName(String categoryName){
//        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);
//        optionalCategory.orElseThrow(() ->
//                new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
//    }

    public void findVerifiedCategoryId(Long categoryId){
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        optionalCategory.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<SubCategory> findSubCategoriesByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
        return category.getSubCategories();
    }
}
