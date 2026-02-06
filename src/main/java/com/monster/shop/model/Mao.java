package com.monster.shop.model;

public class Mao {
    private Integer maoID;
    private String maoName;
    private String username;
    private String password;
    private String dungeonAddress;
    // [新增] 錢包
    private Double wallet;

    public Mao() {}

    public Mao(String maoName, String username, String password, String dungeonAddress) {
        this.maoName = maoName;
        this.username = username;
        this.password = password;
        this.dungeonAddress = dungeonAddress;
        this.wallet = 10000.0; // 預設值
    }

    // Getters and Setters
    public Integer getMaoID() { return maoID; }
    public void setMaoID(Integer maoID) { this.maoID = maoID; }
    
    public String getMaoName() { return maoName; }
    public void setMaoName(String maoName) { this.maoName = maoName; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getDungeonAddress() { return dungeonAddress; }
    public void setDungeonAddress(String dungeonAddress) { this.dungeonAddress = dungeonAddress; }
    
    // [新增] Wallet Getter/Setter
    public Double getWallet() { return wallet; }
    public void setWallet(Double wallet) { this.wallet = wallet; }
}