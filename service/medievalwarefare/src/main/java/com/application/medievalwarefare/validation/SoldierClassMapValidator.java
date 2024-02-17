package com.application.medievalwarefare.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.application.medievalwarefare.dto.Soldier;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SoldierClassMapValidator implements ConstraintValidator<ValidUnitClassMap, Map<String, Integer>> {

	private static final List<Soldier> ALLOWED_UNIT_CLASSES = Arrays.asList(
			Soldier.Militia,
			Soldier.Spearmen,
			Soldier.LightCavalry,
			Soldier.HeavyCavalry,
			Soldier.FootArcher,
			Soldier.CavalryArcher);

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean isValid(Map<String, Integer> value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}

		for (String key : value.keySet()) {
			if (!ALLOWED_UNIT_CLASSES.contains(key)) {
				return false;
			}
		}

		return true;
	}
}
