package com.example.DA.service;

import com.example.DA.model.enums_entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(Category category);

    List<Category> insertCategories(List<Category> categories);

    List<Category> listCategories();
}
