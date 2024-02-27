package com.application.medievalwarefare.service;

import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.application.medievalwarefare.dto.Soldier;
import com.application.medievalwarefare.dto.Terrain;
import com.application.medievalwarefare.entity.BattleOutcome;
import com.application.medievalwarefare.interfaces.TerrainEffectProvider;
import com.application.medievalwarefare.service.terrain.TerrainEffectProviderFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class BattleCalculator {
	@Autowired
	private SoldierAdvantageProvider advantageProvider;

	@Autowired
	private TerrainEffectProviderFactory terrainEffectProviderFactory;

	private Terrain[] terrain;

	public Set<BattleOutcome> calculateOutcome(
			Map.Entry<Soldier, Integer> yourEntry, Map<Soldier, Integer> opponentUnits) {

		Set<BattleOutcome> outcomes = new HashSet<>();

		for (Map.Entry<Soldier, Integer> opponentUnit : opponentUnits.entrySet()) {

			Set<BattleOutcome> currectOutcome = terrainResult(yourEntry, opponentUnit);

			if (currectOutcome.size() > 1) {
				outcomes.addAll(currectOutcome);
			}
		}

		return outcomes;
	}

	private Set<BattleOutcome> terrainResult(
			Map.Entry<Soldier, Integer> yourEntry, Map.Entry<Soldier, Integer> opponentUnit) {
		Set<BattleOutcome> outcomes = new HashSet<>();

		for (Terrain terrainType : terrain) {
			TerrainEffectProvider terrainEffectProvider =
					terrainEffectProviderFactory.getTerrainEffectProvider(terrainType);

			if (terrainEffectProvider == null) continue;

			Float currentYourTerrain =
					yourEntry.getValue() * terrainEffectProvider.getTerrainEffects(yourEntry.getKey());
			Float currentOpponentTerrain =
					opponentUnit.getValue() * terrainEffectProvider.getTerrainEffects(opponentUnit.getKey());

			
					Float outcome ;
			if(hasAdvantage(yourEntry.getKey(), opponentUnit.getKey())) {

				 outcome = currentYourTerrain * 2 - currentOpponentTerrain;
			}else {

			 outcome = currentYourTerrain  - currentOpponentTerrain;
			}


			boolean isWin = Float.compare(Math.max(0, outcome), 0) > 0;

			if (isWin) {
				outcomes.add(new BattleOutcome(
						yourEntry.getKey().name(), opponentUnit.getKey().name(), "win", terrainType));
			}
		}

		return outcomes;
	}

	private boolean hasAdvantage(Soldier yourClass, Soldier opponentClass) {
		return advantageProvider.hasAdvantage(yourClass, opponentClass);
	}
}
