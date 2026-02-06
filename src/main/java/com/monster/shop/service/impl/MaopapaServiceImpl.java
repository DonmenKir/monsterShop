package com.monster.shop.service.impl;

import com.monster.shop.dao.MaopapaDao;
import com.monster.shop.dao.impl.MaopapaDaoImpl;
import com.monster.shop.model.Maopapa;
import com.monster.shop.service.MaopapaService;

public class MaopapaServiceImpl implements MaopapaService {
    private MaopapaDao dao = new MaopapaDaoImpl();

    @Override
    public Maopapa login(String username, String password) {
        return dao.queryMaopapa(username, password);
    }
}