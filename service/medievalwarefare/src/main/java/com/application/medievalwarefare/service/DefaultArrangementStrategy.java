package com.application.medievalwarefare.service;

import java.util.*;

import com.application.medievalwarefare.dto.Soldier;
import com.application.medievalwarefare.entity.BattleEntity;
import com.application.medievalwarefare.entity.BattleOutcome;
import com.application.medievalwarefare.exception.InsufficientWinsException;
import com.application.medievalwarefare.interfaces.ArrangementStrategyInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DefaultArrangementStrategy implements ArrangementStrategyInterface {

	@Autowired
	private BattleCalculator battleCalculator;

	private int minmumWinCount = 3;

	@Override
	public Set<BattleOutcome> findWinningArrangement(BattleEntity battleEntity) {
		Map<Soldier, Integer> yourPlatoons = battleEntity.getYourPlatoons();
		Map<Soldier, Integer> opponentPlatoons = battleEntity.getOpponentPlatoons();

		Set<BattleOutcome> outcomes = new HashSet<>();

		battleCalculator.setTerrain(battleEntity.getTerrain());

		for (Map.Entry<Soldier, Integer> yourEntry : yourPlatoons.entrySet()) {
			Set<BattleOutcome> currentOutcomes = battleCalculator.calculateOutcome(yourEntry, opponentPlatoons);

			if (currentOutcomes.size() > 0) outcomes.addAll(currentOutcomes);

			if (outcomes.size() >= 5) break;
		}

		if (outcomes.isEmpty() || outcomes.size() < minmumWinCount) {
			throw new InsufficientWinsException(HttpStatus.BAD_REQUEST, "The number of wins is insufficient.");
		}

		return new HashSet<>(new ArrayList<>(outcomes).subList(0, Math.min(outcomes.size(), 5)));
	}
}
