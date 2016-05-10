package org.eltech.ddm.associationrules.apriori.dhp.steps;

import org.eltech.ddm.associationrules.apriori.dhp.DHPMiningModel;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.algorithms.Step;
import org.eltech.ddm.miningcore.miningfunctionsettings.EMiningFunctionSettings;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class SetTransactionCountStep extends Step{

	public SetTransactionCountStep(EMiningFunctionSettings settings) throws MiningException {
		super(settings);
	}

	@Override
	protected EMiningModel execute(MiningInputStream inputData, EMiningModel model) throws MiningException {
		DHPMiningModel modelA = (DHPMiningModel) model;
		modelA.setTransactionCount(modelA.getTransactionList().size());
		return modelA;
	}

}
