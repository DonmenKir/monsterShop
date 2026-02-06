package com.monster.shop.service;

import java.util.List;
import com.monster.shop.model.Category;
import com.monster.shop.model.Monster;

public interface MonsterService {
    List<Monster> getAllMonsters();
    List<Category> getAllCategories();
    
    // [新增] 根據分類ID查詢魔物
    List<Monster> getMonstersByCategory(int categoryId);
    
    // 管理員功能
    void addMonster(Monster m);
    void updateMonster(Monster m);
    void deleteMonster(int id);
}