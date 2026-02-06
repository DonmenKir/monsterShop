package com.monster.shop.service.impl;

import java.util.List;
import com.monster.shop.dao.BattleDao;
import com.monster.shop.dao.impl.BattleDaoImpl;
import com.monster.shop.model.ArmyUnit;
import com.monster.shop.service.BattleService;

public class BattleServiceImpl implements BattleService {
    
    private BattleDao battleDao = new BattleDaoImpl();

    @Override
    public List<ArmyUnit> getMyArmy(int maoId) {
        return battleDao.getMaoArmy(maoId);
    }

    @Override
    public int calculateTotalDPS(int maoId) {
        List<ArmyUnit> army = battleDao.getMaoArmy(maoId);
        int totalDps = 0;
        for (ArmyUnit unit : army) {
            totalDps += unit.getTotalDamage();
        }
        return totalDps;
    }

    // [新增]
    @Override
    public void processUnitDeath(int maoId, int monsterId) {
        battleDao.recordBattleCasualty(maoId, monsterId);
    }
}