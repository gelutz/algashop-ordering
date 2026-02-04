package com.lutz.algashop.ordering.domain.utils;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;
import io.hypersistence.tsid.TSID;

import java.util.UUID;

public class IdGenerator {

	private static final TimeBasedEpochRandomGenerator randomGenerator = Generators.timeBasedEpochRandomGenerator();
	private static final TSID.Factory tsidFactory = TSID.Factory.INSTANCE;

	public static UUID generateTimeBasedUUID() {
		return IdGenerator.randomGenerator.generate();
	}


	/**
	 * Env vars TSID_NODE and TSID_NODE_COUNT need to be specified in prod
	 */
	public static TSID generateTSID() {
		return tsidFactory.generate();
	}
}
