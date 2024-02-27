package com.application.medievalwarefare.interfaces;

import java.util.Set;

import com.application.medievalwarefare.entity.BattleEntity;
import com.application.medievalwarefare.entity.BattleOutcome;

public interface ArrangementStrategyInterface {
	Set<BattleOutcome> findWinningArrangement(BattleEntity battleEntity);
}
