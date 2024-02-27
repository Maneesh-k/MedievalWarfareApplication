package com.application.medievalwarefare.service;

import java.util.Map;
import java.util.Set;

import com.application.medievalwarefare.dto.Soldier;
import org.springframework.stereotype.Service;

@Service
public class SoldierAdvantageProvider {
	private final Map<Soldier, Set<Soldier>> advantages;

	public SoldierAdvantageProvider() {
		this.advantages = Map.of(
				Soldier.Militia, Set.of(Soldier.Spearmen, Soldier.LightCavalry),
				Soldier.Spearmen, Set.of(Soldier.LightCavalry, Soldier.HeavyCavalry),
				Soldier.LightCavalry, Set.of(Soldier.FootArcher, Soldier.CavalryArcher),
				Soldier.HeavyCavalry, Set.of(Soldier.Militia, Soldier.FootArcher, Soldier.LightCavalry),
				Soldier.FootArcher, Set.of(Soldier.Spearmen, Soldier.HeavyCavalry),
				Soldier.CavalryArcher, Set.of(Soldier.Militia, Soldier.CavalryArcher));
	}

	public boolean hasAdvantage(Soldier yourClass, Soldier opponentClass) {
		Set<Soldier> advantageSet = advantages.get(yourClass);
		return advantageSet != null && advantageSet.contains(opponentClass);
	}
}
