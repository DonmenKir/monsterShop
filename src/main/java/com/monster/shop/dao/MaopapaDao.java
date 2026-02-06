package com.monster.shop.dao;

import com.monster.shop.model.Maopapa;

public interface MaopapaDao {
    // 驗證魔王爸爸登入
    Maopapa queryMaopapa(String username, String password);
}