package com.monster.shop.dao;

import java.util.List;
import com.monster.shop.model.Category;

public interface CategoryDao {
    // Get all categories for dropdown menu
    List<Category> selectAll();
}