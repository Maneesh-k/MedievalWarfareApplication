package com.application.medievalwarefare.dto;

import java.util.Map;

import jakarta.validation.constraints.NotNull;

public record BattleRequestDTO(
		@NotNull Map<Soldier, Integer> yourPlatoons, @NotNull Map<Soldier, Integer> opponentPlatoons) {}
