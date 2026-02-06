package com.monster.shop.service;

import com.monster.shop.model.Mao;

public interface MaoService {
    Mao login(String username, String password);
    boolean register(Mao mao);
    
    // [新增]
    Mao getMaoById(int id);
}