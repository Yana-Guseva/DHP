package org.eltech.ddm.associationrules.apriori.dhp.steps;

import org.eltech.ddm.associationrules.apriori.dhp.DHPMiningModel;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.algorithms.DecisionStep;
import org.eltech.ddm.miningcore.miningfunctionsettings.EMiningFunctionSettings;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class IsThereCurrentTransaction extends DecisionStep{

	
	
	public IsThereCurrentTransaction(EMiningFunctionSettings settings, CreateHashTable createHashTable) throws MiningException {
		super(settings, createHashTable);
	}

	@Override
	protected boolean condition(MiningInputStream inputData, EMiningModel model) throws MiningException {
		return (!((DHPMiningModel)model).isTransactionPruned());
	}


}
