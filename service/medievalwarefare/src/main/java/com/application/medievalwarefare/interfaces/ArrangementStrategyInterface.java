package com.application.medievalwarefare.interfaces;

import java.util.List;

import com.application.medievalwarefare.entity.BattleEntity;
import com.application.medievalwarefare.entity.BattleOutcome;

public interface ArrangementStrategyInterface {
	List<BattleOutcome> findWinningArrangement(BattleEntity battleEntity);
}
