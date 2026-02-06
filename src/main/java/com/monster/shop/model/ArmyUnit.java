package com.monster.shop.model;

public class ArmyUnit {
    private Integer monsterId; // [新增] 必須知道 ID 才能刪除
    private String monsterName; 
    private int singleAttack;   
    private int totalCount;     
    private int totalDamage;    

    public ArmyUnit() {}

    public ArmyUnit(Integer monsterId, String monsterName, int singleAttack, int totalCount) {
        this.monsterId = monsterId;
        this.monsterName = monsterName;
        this.singleAttack = singleAttack;
        this.totalCount = totalCount;
        this.totalDamage = singleAttack * totalCount;
    }

    public Integer getMonsterId() { return monsterId; }
    public void setMonsterId(Integer monsterId) { this.monsterId = monsterId; }

    public String getMonsterName() { return monsterName; }
    public void setMonsterName(String monsterName) { this.monsterName = monsterName; }

    public int getSingleAttack() { return singleAttack; }
    public void setSingleAttack(int singleAttack) { this.singleAttack = singleAttack; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { 
        this.totalCount = totalCount; 
        this.totalDamage = this.singleAttack * this.totalCount; 
    }

    public int getTotalDamage() { return totalDamage; }
}