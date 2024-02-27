package com.application.medievalwarefare.service.terrain;

import com.application.medievalwarefare.dto.Soldier;
import com.application.medievalwarefare.interfaces.TerrainEffectProvider;
import org.springframework.stereotype.Service;

@Service
public class HillTerrainEffectProvider implements TerrainEffectProvider {

	@Override
	public Float getTerrainEffects(Soldier soldier) {

		switch (soldier) {
			case LightCavalry:
			case HeavyCavalry:
			case Militia:
			case Spearmen:
				return 0.5f;
			case FootArcher:
			case CavalryArcher:
				return 2f;
			default:
				return 1f;
		}
	}
}
