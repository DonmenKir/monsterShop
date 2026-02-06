package com.monster.shop.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.monster.shop.dao.CategoryDao;
import com.monster.shop.model.Category;
import com.monster.shop.util.Tool;

public class CategoryDaoImpl implements CategoryDao {
    @Override
    public List<Category> selectAll() {
        String sql = "SELECT * FROM category";
        List<Category> list = new ArrayList<>();
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryID(rs.getInt("CategoryID"));
                c.setCategoryName(rs.getString("CategoryName"));
                c.setDescription(rs.getString("Description"));
                list.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}