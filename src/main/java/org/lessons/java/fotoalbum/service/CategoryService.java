package org.lessons.java.fotoalbum.service;

import org.lessons.java.fotoalbum.model.Category;
import org.lessons.java.fotoalbum.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll(Sort.by("name"));
    }

    public Category create(Category formCategory) {
        Category categoryToCreate = new Category();
        categoryToCreate.setName(formCategory.getName());
        return categoryRepository.save(categoryToCreate);
    }

    public Category update(Category formCategory) {
        // verifico che l'oggetto ci sia
        //...
        return categoryRepository.save(formCategory);
    }

    public Category getById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

}
