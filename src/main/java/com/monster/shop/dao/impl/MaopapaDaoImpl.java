package com.monster.shop.dao.impl;

import java.sql.*;
import com.monster.shop.dao.MaopapaDao;
import com.monster.shop.model.Maopapa;
import com.monster.shop.util.Tool;

public class MaopapaDaoImpl implements MaopapaDao {

    @Override
    public Maopapa queryMaopapa(String username, String password) {
        // [修改] 表名改為 Maopapa
        String sql = "SELECT * FROM Maopapa WHERE username=? AND password=?";
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Maopapa m = new Maopapa();
                // [修改] 欄位名稱改為 MaopapaID 等
                m.setMaopapaID(rs.getInt("MaopapaID"));
                m.setMaopapaName(rs.getString("MaopapaName"));
                m.setUsername(rs.getString("Username"));
                m.setPassword(rs.getString("Password"));
                m.setMaopapaAddress(rs.getString("MaopapaAddress"));
                return m;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}