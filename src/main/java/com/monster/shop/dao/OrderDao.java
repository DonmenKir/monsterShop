package com.monster.shop.dao;

import java.util.List;
import com.monster.shop.model.OrderDetail;
import com.monster.shop.model.Orders;

public interface OrderDao {
    // 建立訂單 (包含明細)
    void createOrder(Orders order, List<OrderDetail> details);
    
    // 查詢所有訂單
    List<Orders> selectAllOrders();
    
    // 查詢訂單明細
    List<OrderDetail> selectDetailsByOrderId(int orderId);
    
    // [新增] 刪除訂單
    void delete(int orderId);
}