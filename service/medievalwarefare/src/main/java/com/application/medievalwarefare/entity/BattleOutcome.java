package com.application.medievalwarefare.entity;

import com.application.medievalwarefare.dto.Terrain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BattleOutcome {
	private String ownPlatoon;

	private String opponentPlatoon;

	private String outcome;

	private Terrain terrain;
}
