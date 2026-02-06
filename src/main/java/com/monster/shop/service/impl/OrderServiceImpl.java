package com.monster.shop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.monster.shop.dao.MaoDao;
import com.monster.shop.dao.OrderDao;
import com.monster.shop.dao.impl.MaoDaoImpl;
import com.monster.shop.dao.impl.OrderDaoImpl;
import com.monster.shop.model.Mao;
import com.monster.shop.model.Monster;
import com.monster.shop.model.OrderDetail;
import com.monster.shop.model.Orders;
import com.monster.shop.service.OrderService;

public class OrderServiceImpl implements OrderService {
    private OrderDao orderDao = new OrderDaoImpl();
    private MaoDao maoDao = new MaoDaoImpl();

    @Override
    public void checkout(Mao mao, String assistant, Map<Monster, Integer> cart) {
        Orders order = new Orders();
        order.setMaoID(mao.getMaoID());
        order.setMaoAssistant(assistant);
        
        List<OrderDetail> details = new ArrayList<>();
        int totalQty = 0;
        double totalPrice = 0.0;
        
        for (Map.Entry<Monster, Integer> entry : cart.entrySet()) {
            Monster m = entry.getKey();
            Integer qty = entry.getValue();
            
            Double subTotal = m.getPrice() * qty;
            
            totalQty += qty;
            totalPrice += subTotal;
            
            OrderDetail detail = new OrderDetail();
            detail.setMonsterID(m.getMonsterID());
            detail.setMaoID(mao.getMaoID()); // [修改] 在這裡設定 MaoID
            detail.setQuantity(qty);
            detail.setSubTotal(subTotal);
            
            details.add(detail);
        }
        
        order.setTotalQuantity(totalQty);
        order.setTotalPrice(totalPrice);
        
        // 檢查餘額
        double currentBalance = mao.getWallet();
        if (currentBalance < totalPrice) {
            throw new RuntimeException("錢包餘額不足！尚缺 $" + (totalPrice - currentBalance));
        }
        
        // 扣款
        double newBalance = currentBalance - totalPrice;
        mao.setWallet(newBalance); 
        maoDao.updateWallet(mao.getMaoID(), newBalance); 
        
        // 建立訂單
        orderDao.createOrder(order, details);
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderDao.selectAllOrders();
    }

    @Override
    public List<OrderDetail> getOrderDetails(int orderId) {
        return orderDao.selectDetailsByOrderId(orderId);
    }

    @Override
    public void deleteOrder(int orderId) {
        orderDao.delete(orderId);
    }
}