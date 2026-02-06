package com.monster.shop.model;

public class Maopapa {
    // [修改] 變數名稱全面更新為 maopapa 開頭
    private Integer maopapaID;
    private String maopapaName;
    private String username;
    private String password;
    private String maopapaAddress;

    public Maopapa() {}

    public Maopapa(String maopapaName, String username, String password, String maopapaAddress) {
        this.maopapaName = maopapaName;
        this.username = username;
        this.password = password;
        this.maopapaAddress = maopapaAddress;
    }

    // Getters and Setters 也隨之更新
    public Integer getMaopapaID() { return maopapaID; }
    public void setMaopapaID(Integer maopapaID) { this.maopapaID = maopapaID; }
    
    public String getMaopapaName() { return maopapaName; }
    public void setMaopapaName(String maopapaName) { this.maopapaName = maopapaName; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getMaopapaAddress() { return maopapaAddress; }
    public void setMaopapaAddress(String maopapaAddress) { this.maopapaAddress = maopapaAddress; }
}