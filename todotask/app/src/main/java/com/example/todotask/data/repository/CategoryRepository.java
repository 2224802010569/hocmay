package com.example.todotask.data.repository;

import android.content.Context;
import com.example.todotask.data.dao.CategoryDao;
import com.example.todotask.data.model.Category;
import com.example.todotask.data.database.DatabaseHelper;
import java.util.List;


public class CategoryRepository {
    private final CategoryDao dao;

    public CategoryRepository(Context context) {
        dao = new CategoryDao(context);
    }

    public List<Category> getAll() {
        return dao.getAllCategories();
    }

    public boolean add(Category category) {
        return dao.addCategory(category) != -1;
    }

    public void delete(int id) {
        dao.deleteCategory(id);
    }

    public void update(Category category) {
        dao.updateCategory(category);
    }
    public Category getById(int id) {
        return dao.getCategoryById(id);
    }


}
