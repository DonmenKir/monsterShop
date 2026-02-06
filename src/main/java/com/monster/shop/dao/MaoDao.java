package com.monster.shop.dao;

import com.monster.shop.model.Mao;

public interface MaoDao {
    boolean checkUsername(String username);
    void add(Mao mao);
    Mao queryMao(String username, String password);
    Mao findById(int id);
    
    // [新增] 更新錢包餘額
    void updateWallet(int maoId, double newBalance);
}