package com.monster.shop.service;

import java.util.List;
import java.util.Map;
import com.monster.shop.model.Mao;
import com.monster.shop.model.Monster;
import com.monster.shop.model.OrderDetail;
import com.monster.shop.model.Orders;

public interface OrderService {
    // 結帳邏輯
    void checkout(Mao mao, String assistant, Map<Monster, Integer> cart);
    
    // 查詢所有訂單
    List<Orders> getAllOrders();
    
    // 查詢單筆訂單明細
    List<OrderDetail> getOrderDetails(int orderId);
    
    // [新增] 刪除訂單
    void deleteOrder(int orderId);
}