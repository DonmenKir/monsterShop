package com.monster.shop.service.impl;

import com.monster.shop.dao.MaoDao;
import com.monster.shop.dao.impl.MaoDaoImpl;
import com.monster.shop.model.Mao;
import com.monster.shop.service.MaoService;

public class MaoServiceImpl implements MaoService {
    private MaoDao dao = new MaoDaoImpl();

    @Override
    public Mao login(String username, String password) {
        return dao.queryMao(username, password);
    }

    @Override
    public boolean register(Mao mao) {
        if (dao.checkUsername(mao.getUsername())) {
            return false; 
        }
        dao.add(mao);
        return true;
    }

    // [新增]
    @Override
    public Mao getMaoById(int id) {
        return dao.findById(id);
    }
}