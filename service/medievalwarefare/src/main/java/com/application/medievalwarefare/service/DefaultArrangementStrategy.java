package com.application.medievalwarefare.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.application.medievalwarefare.dto.Soldier;
import com.application.medievalwarefare.entity.BattleEntity;
import com.application.medievalwarefare.entity.BattleOutcome;
import com.application.medievalwarefare.exception.InsufficientWinsException;
import com.application.medievalwarefare.interfaces.ArrangementStrategyInterface;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DefaultArrangementStrategy implements ArrangementStrategyInterface {
	@Override
	public List<BattleOutcome> findWinningArrangement(BattleEntity battleEntity) {
		Map<Soldier, Integer> yourPlatoons = battleEntity.getYourPlatoons();

		Map<Soldier, Integer> opponentPlatoons = battleEntity.getOpponentPlatoons();

		List<Soldier> yourSoldiers = new ArrayList<>(yourPlatoons.keySet());

		List<BattleOutcome> outcomes = new ArrayList<>();

		int winCount = 0;

		for (List<Soldier> arrangement : permutations(yourSoldiers)) {
			Map<Soldier, Integer> yourUnits = new LinkedHashMap<>(yourPlatoons);

			Map<Soldier, Integer> opponentUnits = new LinkedHashMap<>(opponentPlatoons);

			int wins = calculateOutcome(yourUnits, opponentUnits);

			if (wins >= 3) {
				Soldier ownUnit = arrangement.get(0);

				Soldier opponentUnit = getBestOpponent(opponentPlatoons.keySet(), ownUnit);

				outcomes.add(new BattleOutcome(ownUnit.name(), opponentUnit.name(), "win"));

				winCount++;

				if (winCount >= 5) return outcomes;
			}
		}

		if (outcomes.isEmpty() || winCount < 3)
			throw new InsufficientWinsException(HttpStatus.BAD_REQUEST, "The number of wins is insufficient.");

		return outcomes;
	}

	private Soldier getBestOpponent(Set<Soldier> opponentClasses, Soldier ownUnit) {
		return opponentClasses.stream()
				.filter(opponentClass -> hasAdvantage(ownUnit, opponentClass))
				.findFirst()
				.orElseThrow();
	}

	private int calculateOutcome(Map<Soldier, Integer> yourUnits, Map<Soldier, Integer> opponentUnits) {
		int wins = 0;

		for (Map.Entry<Soldier, Integer> yourEntry : yourUnits.entrySet()) {
			Soldier yourClass = yourEntry.getKey();

			Soldier opponentClass = getBestOpponent(opponentUnits.keySet(), yourClass);

			if (hasAdvantage(yourClass, opponentClass)) {
				int outcome = yourEntry.getValue() * 2 - opponentUnits.get(opponentClass);

				wins += Math.max(0, outcome);
			}
		}

		return wins;
	}

	private boolean hasAdvantage(Soldier yourClass, Soldier opponentClass) {
		Map<Soldier, Set<Soldier>> advantages = Map.of(
				Soldier.Militia, Set.of(Soldier.Spearmen, Soldier.LightCavalry),
				Soldier.Spearmen, Set.of(Soldier.LightCavalry, Soldier.HeavyCavalry),
				Soldier.LightCavalry, Set.of(Soldier.FootArcher, Soldier.CavalryArcher),
				Soldier.HeavyCavalry, Set.of(Soldier.Militia, Soldier.FootArcher, Soldier.LightCavalry),
				Soldier.FootArcher, Set.of(Soldier.Spearmen, Soldier.HeavyCavalry),
				Soldier.CavalryArcher, Set.of(Soldier.Militia, Soldier.CavalryArcher));

		Set<Soldier> advantageSet = advantages.get(yourClass);
		return advantageSet != null && advantageSet.contains(opponentClass);
	}

	private <T> List<List<T>> permutations(List<T> elements) {
		List<List<T>> result = new ArrayList<>();
		permute(elements, 0, result);
		return result;
	}

	private <T> void permute(List<T> elements, int start, List<List<T>> result) {
		if (start == elements.size() - 1) {
			result.add(new ArrayList<>(elements));
		} else {
			for (int i = start; i < elements.size(); i++) {
				Collections.swap(elements, start, i);
				permute(elements, start + 1, result);
				Collections.swap(elements, start, i);
			}
		}
	}
}
