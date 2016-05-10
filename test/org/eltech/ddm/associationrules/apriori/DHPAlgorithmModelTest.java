package org.eltech.ddm.associationrules.apriori;

import static org.junit.Assert.*;

import org.eltech.ddm.associationrules.AssociationRulesFunctionSettings;
import org.eltech.ddm.associationrules.apriori.dhp.DHPAlgorithm;
import org.eltech.ddm.associationrules.apriori.dhp.DHPMiningModel;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.junit.Test;

public class DHPAlgorithmModelTest extends AprioriModelTest{

	@Override
	protected DHPMiningModel buildModel(AssociationRulesFunctionSettings miningSettings, MiningInputStream inputData)
			throws MiningException {
		DHPAlgorithm algorithm = new DHPAlgorithm(miningSettings);
		DHPMiningModel model = (DHPMiningModel) algorithm.buildModel(inputData);

		System.out.println("calculation time [s]: " + algorithm.getTimeSpentToBuildModel());

		return model;
	}

}
