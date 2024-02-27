package com.application.medievalwarefare.service.terrain;

import com.application.medievalwarefare.dto.Terrain;
import com.application.medievalwarefare.interfaces.TerrainEffectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerrainEffectProviderFactory {

	@Autowired
	private PlainTerrainEffectProvider plainTerrainEffectProvider;

	@Autowired
	private HillTerrainEffectProvider hillTerrainEffectProvider;

	@Autowired
	private MuddyTerrainEffectProvider muddyTerrainEffectProvider;

	public TerrainEffectProvider getTerrainEffectProvider(Terrain terrain) {
		switch (terrain) {
			case PLAINS:
				return plainTerrainEffectProvider;
			case HILL:
				return hillTerrainEffectProvider;
			case MUDDY:
				return muddyTerrainEffectProvider;
			default:
				return null;
		}
	}
}
