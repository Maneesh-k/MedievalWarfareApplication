package com.application.medievalwarefare.mapper;

import com.application.medievalwarefare.dto.BattleRequestDTO;
import com.application.medievalwarefare.entity.BattleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
		componentModel = "spring",
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapStructMapper {
	BattleEntity toBattleEntity(BattleRequestDTO battleRequestDTO);
}
