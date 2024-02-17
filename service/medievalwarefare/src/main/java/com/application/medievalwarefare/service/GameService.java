package com.application.medievalwarefare.service;

import java.util.List;
import java.util.Map;

import com.application.medievalwarefare.dto.BattleRequestDTO;
import com.application.medievalwarefare.entity.BattleEntity;
import com.application.medievalwarefare.entity.BattleOutcome;
import com.application.medievalwarefare.interfaces.ArrangementStrategyInterface;
import com.application.medievalwarefare.mapper.MapStructMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

	@Autowired
	private ArrangementStrategyInterface arrangementStrategy;

	@Autowired
	MapStructMapper mapStructMapper;

	public Map<String, List<BattleOutcome>> findWinningArrangement(BattleRequestDTO requestDTO) {

		BattleEntity battleEntity = mapStructMapper.toBattleEntity(requestDTO);

		List<BattleOutcome> winningArrangement = arrangementStrategy.findWinningArrangement(battleEntity);

		return Map.of("battle", winningArrangement);
	}
}
