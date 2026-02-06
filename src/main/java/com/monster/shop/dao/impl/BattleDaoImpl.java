package com.monster.shop.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.monster.shop.dao.BattleDao;
import com.monster.shop.model.ArmyUnit;
import com.monster.shop.util.Tool;

public class BattleDaoImpl implements BattleDao {

    @Override
    public List<ArmyUnit> getMaoArmy(int maoId) {
        List<ArmyUnit> army = new ArrayList<>();
        
        // [修改] SQL 加入查詢 m.MonsterID
        String sql = "SELECT " +
                     "  m.MonsterID, " +
                     "  m.MonsterName, " +
                     "  m.Attack, " +
                     "  SUM(od.Quantity) as TotalQty " +
                     "FROM orderdetail od " +
                     "JOIN orders o ON od.OrderID = o.OrderID " +
                     "JOIN monster m ON od.MonsterID = m.MonsterID " +
                     "WHERE o.MaoID = ? " +
                     "GROUP BY m.MonsterID, m.MonsterName, m.Attack";

        try (Connection conn = Tool.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, maoId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("MonsterID"); // [新增]
                String name = rs.getString("MonsterName");
                int atk = rs.getInt("Attack");
                int qty = rs.getInt("TotalQty");
                
                // 排除數量 <= 0 的 (可能已死光)
                if (qty > 0) {
                    army.add(new ArmyUnit(id, name, atk, qty));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return army;
    }

    // [新增] 實作：建立一筆數量為 -1 的訂單來扣除庫存
    @Override
    public void recordBattleCasualty(int maoId, int monsterId) {
        String sqlOrder = "INSERT INTO orders(MaoID, MaoAssistant, TotalQuantity, TotalPrice) VALUES(?, '系統(戰損)', -1, 0)";
        String sqlDetail = "INSERT INTO orderdetail(OrderID, MonsterID, Quantity, SubTotal, MaoID) VALUES(?, ?, -1, 0, ?)";
        
        try (Connection conn = Tool.getConnection()) {
            conn.setAutoCommit(false);
            
            // 1. 建立戰損主檔
            PreparedStatement ps1 = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            ps1.setInt(1, maoId);
            ps1.executeUpdate();
            
            ResultSet rs = ps1.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }
            
            // 2. 建立戰損明細 (數量 -1)
            PreparedStatement ps2 = conn.prepareStatement(sqlDetail);
            ps2.setInt(1, orderId);
            ps2.setInt(2, monsterId);
            ps2.setInt(3, maoId);
            ps2.executeUpdate();
            
            conn.commit();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}