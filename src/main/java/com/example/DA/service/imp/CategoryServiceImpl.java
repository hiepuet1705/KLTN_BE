package com.example.DA.service.imp;


import com.example.DA.model.enums_entity.Category;
import com.example.DA.repo.CategoryRepository;
import com.example.DA.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        categoryRepository.save(category);
        return category;
    }

    @Override
    public List<Category> insertCategories(List<Category> categories) {
        return categoryRepository.saveAll(categories);
    }

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }
}
