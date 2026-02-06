package com.monster.shop.model;

import java.util.Date;

public class Orders {
    private Integer orderID;
    private Integer maoID;
    private Date orderDate; // Use java.util.Date
    private String maoAssistant;
    private Integer totalQuantity;
    private Double totalPrice;

    public Orders() {}

    // Getters and Setters
    public Integer getOrderID() { return orderID; }
    public void setOrderID(Integer orderID) { this.orderID = orderID; }
    public Integer getMaoID() { return maoID; }
    public void setMaoID(Integer maoID) { this.maoID = maoID; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public String getMaoAssistant() { return maoAssistant; }
    public void setMaoAssistant(String maoAssistant) { this.maoAssistant = maoAssistant; }
    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}