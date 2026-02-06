package com.monster.shop.service.impl;

import java.util.List;
import com.monster.shop.dao.CategoryDao;
import com.monster.shop.dao.MonsterDao;
import com.monster.shop.dao.impl.CategoryDaoImpl;
import com.monster.shop.dao.impl.MonsterDaoImpl;
import com.monster.shop.model.Category;
import com.monster.shop.model.Monster;
import com.monster.shop.service.MonsterService;

public class MonsterServiceImpl implements MonsterService {
    private MonsterDao monsterDao = new MonsterDaoImpl();
    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Monster> getAllMonsters() {
        return monsterDao.selectAll();
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.selectAll();
    }

    // [新增] 實作分類查詢
    @Override
    public List<Monster> getMonstersByCategory(int categoryId) {
        // 如果是 0 或負數，代表"全部"，回傳所有魔物
        if (categoryId <= 0) {
            return monsterDao.selectAll();
        }
        return monsterDao.selectByCategory(categoryId);
    }

    @Override
    public void addMonster(Monster m) {
        monsterDao.add(m);
    }

    @Override
    public void updateMonster(Monster m) {
        monsterDao.update(m);
    }

    @Override
    public void deleteMonster(int id) {
        monsterDao.delete(id);
    }
}