package com.monster.shop.model;

public class Monster {
    private Integer monsterID;
    private String monsterName;
    private Integer categoryID;
    private String categoryName;
    private Double price;
    private Integer attack;
    private Integer maxHP;
    private Integer attackRange;
    // [新增] 速度
    private Integer speed;

    public Monster() {}

    public Monster(String monsterName, Integer categoryID, Double price, Integer attack, Integer maxHP, Integer range, Integer speed) {
        this.monsterName = monsterName;
        this.categoryID = categoryID;
        this.price = price;
        this.attack = attack;
        this.maxHP = maxHP;
        this.attackRange = range;
        this.speed = speed;
    }

    // Getters and Setters
    public Integer getMonsterID() { return monsterID; }
    public void setMonsterID(Integer monsterID) { this.monsterID = monsterID; }
    
    public String getMonsterName() { return monsterName; }
    public void setMonsterName(String monsterName) { this.monsterName = monsterName; }
    
    public Integer getCategoryID() { return categoryID; }
    public void setCategoryID(Integer categoryID) { this.categoryID = categoryID; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Integer getAttack() { return attack; }
    public void setAttack(Integer attack) { this.attack = attack; }
    
    public Integer getMaxHP() { return maxHP; }
    public void setMaxHP(Integer maxHP) { this.maxHP = maxHP; }

    public Integer getAttackRange() { return attackRange; }
    public void setAttackRange(Integer attackRange) { this.attackRange = attackRange; }

    // [新增]
    public Integer getSpeed() { return speed; }
    public void setSpeed(Integer speed) { this.speed = speed; }
    
    @Override
    public String toString() {
        return monsterName; 
    }
}