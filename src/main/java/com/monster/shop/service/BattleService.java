package com.monster.shop.service;

import java.util.List;
import com.monster.shop.model.ArmyUnit;

public interface BattleService {
    List<ArmyUnit> getMyArmy(int maoId);
    int calculateTotalDPS(int maoId);
    
    // [新增] 處理魔物死亡
    void processUnitDeath(int maoId, int monsterId);
}