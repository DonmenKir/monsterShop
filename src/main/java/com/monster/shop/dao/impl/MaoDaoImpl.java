package com.monster.shop.dao.impl;

import java.sql.*;
import com.monster.shop.dao.MaoDao;
import com.monster.shop.model.Mao;
import com.monster.shop.util.Tool;

public class MaoDaoImpl implements MaoDao {

    @Override
    public boolean checkUsername(String username) {
        String sql = "SELECT count(*) FROM mao WHERE username = ?";
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public void add(Mao mao) {
        String sql = "INSERT INTO mao(MaoName, Username, Password, DungeonAddress, Wallet) VALUES(?,?,?,?,?)";
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mao.getMaoName());
            ps.setString(2, mao.getUsername());
            ps.setString(3, mao.getPassword());
            ps.setString(4, mao.getDungeonAddress());
            ps.setDouble(5, 10000.0); 
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public Mao queryMao(String username, String password) {
        String sql = "SELECT * FROM mao WHERE username=? AND password=?";
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractMao(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Mao findById(int id) {
        String sql = "SELECT * FROM mao WHERE MaoID=?";
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractMao(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // [新增] 實作更新錢包
    @Override
    public void updateWallet(int maoId, double newBalance) {
        String sql = "UPDATE mao SET Wallet=? WHERE MaoID=?";
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, maoId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private Mao extractMao(ResultSet rs) throws SQLException {
        Mao m = new Mao();
        m.setMaoID(rs.getInt("MaoID"));
        m.setMaoName(rs.getString("MaoName"));
        m.setUsername(rs.getString("Username"));
        m.setPassword(rs.getString("Password"));
        m.setDungeonAddress(rs.getString("DungeonAddress"));
        m.setWallet(rs.getDouble("Wallet")); 
        return m;
    }
}