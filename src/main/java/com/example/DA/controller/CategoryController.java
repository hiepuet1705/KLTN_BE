package com.example.DA.controller;


import com.example.DA.model.enums_entity.Category;
import com.example.DA.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Category>> addCategories(@RequestBody List<Category> categories) {
        List<Category> categoryList = categoryService.insertCategories(categories);
        return new ResponseEntity<>(categoryList, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categoryList = categoryService.listCategories();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);

    }
}
