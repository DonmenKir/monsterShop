package com.monster.shop.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.monster.shop.dao.MonsterDao;
import com.monster.shop.model.Monster;
import com.monster.shop.util.Tool;

public class MonsterDaoImpl implements MonsterDao {

    @Override
    public void add(Monster m) {
        // [修改] 加入 Speed
        String sql = "INSERT INTO monster(MonsterName, CategoryID, CategoryName, Price, Attack, MaxHP, AttackRange, Speed) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getMonsterName());
            ps.setInt(2, m.getCategoryID());
            ps.setString(3, m.getCategoryName());
            ps.setDouble(4, m.getPrice());
            ps.setInt(5, m.getAttack() != null ? m.getAttack() : 10);
            ps.setInt(6, m.getMaxHP() != null ? m.getMaxHP() : 100);
            ps.setInt(7, m.getAttackRange() != null ? m.getAttackRange() : 1);
            ps.setInt(8, m.getSpeed() != null ? m.getSpeed() : 10); // [新增] 預設速度 10
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Monster> selectAll() {
        String sql = "SELECT * FROM monster";
        List<Monster> list = new ArrayList<>();
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractMonster(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public void update(Monster m) {
        // [修改] 加入 Speed 更新
        String sql = "UPDATE monster SET MonsterName=?, CategoryID=?, CategoryName=?, Price=?, Attack=?, MaxHP=?, AttackRange=?, Speed=? WHERE MonsterID=?";
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getMonsterName());
            ps.setInt(2, m.getCategoryID());
            ps.setString(3, m.getCategoryName());
            ps.setDouble(4, m.getPrice());
            ps.setInt(5, m.getAttack());
            ps.setInt(6, m.getMaxHP());
            ps.setInt(7, m.getAttackRange());
            ps.setInt(8, m.getSpeed()); // [新增]
            ps.setInt(9, m.getMonsterID());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM monster WHERE MonsterID=?";
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Monster> selectByCategory(int categoryId) {
        String sql = "SELECT * FROM monster WHERE CategoryID=?";
        List<Monster> list = new ArrayList<>();
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractMonster(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    private Monster extractMonster(ResultSet rs) throws SQLException {
        Monster m = new Monster();
        m.setMonsterID(rs.getInt("MonsterID"));
        m.setMonsterName(rs.getString("MonsterName"));
        m.setCategoryID(rs.getInt("CategoryID"));
        m.setCategoryName(rs.getString("CategoryName"));
        m.setPrice(rs.getDouble("Price"));
        m.setAttack(rs.getInt("Attack"));
        m.setMaxHP(rs.getInt("MaxHP"));
        m.setAttackRange(rs.getInt("AttackRange"));
        m.setSpeed(rs.getInt("Speed")); // [新增]
        return m;
    }
}