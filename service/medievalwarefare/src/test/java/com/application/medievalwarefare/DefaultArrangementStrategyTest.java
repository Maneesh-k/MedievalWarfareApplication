package com.application.medievalwarefare;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.*;

import com.application.medievalwarefare.dto.Soldier;
import com.application.medievalwarefare.entity.BattleEntity;
import com.application.medievalwarefare.entity.BattleOutcome;
import com.application.medievalwarefare.exception.InsufficientWinsException;
import com.application.medievalwarefare.interfaces.ArrangementStrategyInterface;
import com.application.medievalwarefare.service.DefaultArrangementStrategy;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DefaultArrangementStrategyTest {

	@Mock
	private ArrangementStrategyInterface arrangementStrategy;

	@InjectMocks
	private DefaultArrangementStrategy defaultArrangementStrategy;

	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSufficientWins() {
		setUp();

		BattleEntity battleEntity = new BattleEntity();
		Map<Soldier, Integer> yourPlatoons = Map.of(
				Soldier.Militia, 100,
				Soldier.Spearmen, 150,
				Soldier.LightCavalry, 120,
				Soldier.HeavyCavalry, 80,
				Soldier.CavalryArcher, 60);

		Map<Soldier, Integer> opponentPlatoons = Map.of(
				Soldier.Militia, 0,
				Soldier.Spearmen, 30,
				Soldier.LightCavalry, 10,
				Soldier.HeavyCavalry, 0,
				Soldier.CavalryArcher, 40);

		battleEntity.setYourPlatoons(yourPlatoons);
		battleEntity.setOpponentPlatoons(opponentPlatoons);

		List<BattleOutcome> outcomes = defaultArrangementStrategy.findWinningArrangement(battleEntity);

		assertEquals(5, outcomes.size());
	}

	@Test
	void testExactlyFiveWins() {
		setUp();
		BattleEntity battleEntity = new BattleEntity();
		Map<Soldier, Integer> yourPlatoons = Map.of(
				Soldier.Militia, 100,
				Soldier.Spearmen, 150,
				Soldier.LightCavalry, 120,
				Soldier.HeavyCavalry, 80,
				Soldier.CavalryArcher, 60);

		Map<Soldier, Integer> opponentPlatoons = Map.of(
				Soldier.Militia, 0,
				Soldier.Spearmen, 30,
				Soldier.LightCavalry, 10,
				Soldier.HeavyCavalry, 0,
				Soldier.CavalryArcher, 40);

		battleEntity.setYourPlatoons(yourPlatoons);
		battleEntity.setOpponentPlatoons(opponentPlatoons);

		when(arrangementStrategy.findWinningArrangement(battleEntity))
				.thenReturn(Arrays.asList(
						new BattleOutcome("Militia", "Spearmen", "win"),
						new BattleOutcome("Spearmen", "Militia", "win"),
						new BattleOutcome("LightCavalry", "Militia", "win"),
						new BattleOutcome("HeavyCavalry", "Militia", "win"),
						new BattleOutcome("CavalryArcher", "Militia", "win")));

		List<BattleOutcome> outcomes = defaultArrangementStrategy.findWinningArrangement(battleEntity);

		assertEquals(5, outcomes.size());
	}

	@Test
	void testInsufficientWins() {
		setUp();

		BattleEntity battleEntity = new BattleEntity();
		Map<Soldier, Integer> yourPlatoons = Map.of(
				Soldier.Militia, 0,
				Soldier.Spearmen, 0,
				Soldier.LightCavalry, 0,
				Soldier.HeavyCavalry, 0,
				Soldier.CavalryArcher, 0);

		Map<Soldier, Integer> opponentPlatoons = Map.of(
				Soldier.Militia, 30,
				Soldier.Spearmen, 40,
				Soldier.LightCavalry, 60,
				Soldier.HeavyCavalry, 80,
				Soldier.CavalryArcher, 100);

		battleEntity.setYourPlatoons(yourPlatoons);
		battleEntity.setOpponentPlatoons(opponentPlatoons);

		assertThrows(
				InsufficientWinsException.class, () -> defaultArrangementStrategy.findWinningArrangement(battleEntity));
	}
}
