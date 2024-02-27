package com.application.medievalwarefare.controller;

import java.util.Map;
import java.util.Set;

import com.application.medievalwarefare.dto.BattleRequestDTO;
import com.application.medievalwarefare.entity.BattleOutcome;
import com.application.medievalwarefare.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class GameController {
	@Autowired
	private GameService gameService;

	@PostMapping("/battle")
	public ResponseEntity<Map<String, Set<BattleOutcome>>> conductBattle(
			@Valid @RequestBody BattleRequestDTO requestDTO) {
		Map<String, Set<BattleOutcome>> winningArrangement = gameService.findWinningArrangement(requestDTO);

		return ResponseEntity.status(HttpStatus.OK).body(winningArrangement);
	}
}
