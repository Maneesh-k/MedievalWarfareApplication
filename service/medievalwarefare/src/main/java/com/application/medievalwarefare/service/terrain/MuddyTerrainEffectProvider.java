package com.application.medievalwarefare.service.terrain;

import com.application.medievalwarefare.dto.Soldier;
import com.application.medievalwarefare.interfaces.TerrainEffectProvider;
import org.springframework.stereotype.Service;

@Service
public class MuddyTerrainEffectProvider implements TerrainEffectProvider {

	@Override
	public Float getTerrainEffects(Soldier soldier) {

		switch (soldier) {
			case Militia:
			case Spearmen:
			case FootArcher:
				return 2f;
			case LightCavalry:
			case HeavyCavalry:
			case CavalryArcher:
				return 1f;
			default:
				return 1f;
		}
	}
}
