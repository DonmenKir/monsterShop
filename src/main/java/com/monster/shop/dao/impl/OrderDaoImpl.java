package com.monster.shop.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.monster.shop.dao.OrderDao;
import com.monster.shop.model.OrderDetail;
import com.monster.shop.model.Orders;
import com.monster.shop.util.Tool;

public class OrderDaoImpl implements OrderDao {

    @Override
    public void createOrder(Orders order, List<OrderDetail> details) {
        Connection conn = null;
        PreparedStatement psHeader = null;
        PreparedStatement psDetail = null;
        
        String sqlHeader = "INSERT INTO orders(MaoID, MaoAssistant, TotalQuantity, TotalPrice) VALUES(?,?,?,?)";
        // [修改] SQL 語法加入 MaoID
        String sqlDetail = "INSERT INTO orderdetail(OrderID, MonsterID, Quantity, SubTotal, MaoID) VALUES(?,?,?,?,?)";

        try {
            conn = Tool.getConnection();
            conn.setAutoCommit(false); 

            // 1. 新增主檔
            psHeader = conn.prepareStatement(sqlHeader, Statement.RETURN_GENERATED_KEYS);
            psHeader.setInt(1, order.getMaoID());
            psHeader.setString(2, order.getMaoAssistant());
            psHeader.setInt(3, order.getTotalQuantity());
            psHeader.setDouble(4, order.getTotalPrice());
            psHeader.executeUpdate();

            ResultSet rs = psHeader.getGeneratedKeys();
            int newOrderId = 0;
            if (rs.next()) {
                newOrderId = rs.getInt(1);
            } else {
                throw new SQLException("訂單建立失敗，無法取得 ID。");
            }

            // 2. 新增明細 (批次)
            psDetail = conn.prepareStatement(sqlDetail);
            for (OrderDetail d : details) {
                psDetail.setInt(1, newOrderId); 
                psDetail.setInt(2, d.getMonsterID());
                psDetail.setInt(3, d.getQuantity());
                psDetail.setDouble(4, d.getSubTotal());
                psDetail.setInt(5, d.getMaoID()); // [修改] 寫入 MaoID
                psDetail.addBatch();
            }
            psDetail.executeBatch();

            conn.commit(); 

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            try {
                if (psHeader != null) psHeader.close();
                if (psDetail != null) psDetail.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @Override
    public List<Orders> selectAllOrders() {
        String sql = "SELECT * FROM orders ORDER BY OrderDate DESC";
        List<Orders> list = new ArrayList<>();
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Orders o = new Orders();
                o.setOrderID(rs.getInt("OrderID"));
                o.setMaoID(rs.getInt("MaoID"));
                o.setOrderDate(rs.getTimestamp("OrderDate"));
                o.setMaoAssistant(rs.getString("MaoAssistant"));
                o.setTotalQuantity(rs.getInt("TotalQuantity"));
                o.setTotalPrice(rs.getDouble("TotalPrice"));
                list.add(o);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<OrderDetail> selectDetailsByOrderId(int orderId) {
        // [修改] 查詢時也順便把 MaoID 抓出來 (雖然介面暫時沒用到，但保持物件完整性)
        String sql = "SELECT d.*, m.MonsterName FROM orderdetail d " +
                     "JOIN monster m ON d.MonsterID = m.MonsterID " +
                     "WHERE d.OrderID = ?";
        List<OrderDetail> list = new ArrayList<>();
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderDetail d = new OrderDetail();
                d.setDetailID(rs.getInt("DetailID"));
                d.setOrderID(rs.getInt("OrderID"));
                d.setMaoID(rs.getInt("MaoID")); // [修改] 讀取 MaoID
                d.setMonsterID(rs.getInt("MonsterID"));
                d.setMonsterName(rs.getString("MonsterName")); 
                d.setQuantity(rs.getInt("Quantity"));
                d.setSubTotal(rs.getDouble("SubTotal"));
                list.add(d);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public void delete(int orderId) {
        String sql = "DELETE FROM orders WHERE OrderID = ?";
        try (Connection conn = Tool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}