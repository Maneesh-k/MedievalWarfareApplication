package com.application.medievalwarefare.entity;

import java.util.Map;

import com.application.medievalwarefare.dto.Soldier;
import com.application.medievalwarefare.dto.Terrain;
import com.application.medievalwarefare.validation.ValidUnitClassMap;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleEntity {

	@NotNull
	@ValidUnitClassMap
	private Map<Soldier, Integer> yourPlatoons;

	@NotNull
	@ValidUnitClassMap
	private Map<Soldier, Integer> opponentPlatoons;

	@NotNull
	private Terrain[] terrain;
}
