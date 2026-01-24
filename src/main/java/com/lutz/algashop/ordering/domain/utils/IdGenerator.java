package com.lutz.algashop.ordering.domain.utils;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;

import java.util.UUID;

public class IdGenerator {

	private static final TimeBasedEpochRandomGenerator randomGenerator = Generators.timeBasedEpochRandomGenerator();


	public static UUID generateTimeBasedUUID() {
		return IdGenerator.randomGenerator.generate();
	}
}
