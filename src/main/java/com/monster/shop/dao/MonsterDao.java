package com.monster.shop.dao;

import java.util.List;
import com.monster.shop.model.Monster;

public interface MonsterDao {
    // CRUD for Monsters
    void add(Monster m);
    List<Monster> selectAll();
    void update(Monster m);
    void delete(int id);
    
    // Optional: Filter by Category
    List<Monster> selectByCategory(int categoryId);
}