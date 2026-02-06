package com.monster.shop.dao;

import java.util.List;
import com.monster.shop.model.ArmyUnit;

public interface BattleDao {
    List<ArmyUnit> getMaoArmy(int maoId);
    
    // [新增] 紀錄魔物死亡 (扣除庫存)
    void recordBattleCasualty(int maoId, int monsterId);
}