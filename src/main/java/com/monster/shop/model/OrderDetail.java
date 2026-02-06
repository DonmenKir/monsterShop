package com.monster.shop.model;

public class OrderDetail {
    private Integer detailID;
    private Integer orderID;
    private Integer maoID; // [新增]
    private Integer monsterID;
    private Integer quantity;
    private Double subTotal;
    
    // UI顯示用
    private String monsterName; 

    public OrderDetail() {}

    // Getters and Setters
    public Integer getDetailID() { return detailID; }
    public void setDetailID(Integer detailID) { this.detailID = detailID; }
    
    public Integer getOrderID() { return orderID; }
    public void setOrderID(Integer orderID) { this.orderID = orderID; }
    
    // [新增 Getter/Setter]
    public Integer getMaoID() { return maoID; }
    public void setMaoID(Integer maoID) { this.maoID = maoID; }
    
    public Integer getMonsterID() { return monsterID; }
    public void setMonsterID(Integer monsterID) { this.monsterID = monsterID; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public Double getSubTotal() { return subTotal; }
    public void setSubTotal(Double subTotal) { this.subTotal = subTotal; }
    
    public String getMonsterName() { return monsterName; }
    public void setMonsterName(String monsterName) { this.monsterName = monsterName; }
}