package com.monster.shop.service;

import com.monster.shop.model.Maopapa;

public interface MaopapaService {
    // 魔王爸爸登入
    Maopapa login(String username, String password);
}