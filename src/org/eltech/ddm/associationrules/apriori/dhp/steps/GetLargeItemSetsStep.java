package org.eltech.ddm.associationrules.apriori.dhp.steps;

import java.util.List;
import java.util.Map;

import org.eltech.ddm.associationrules.AssociationRulesFunctionSettings;
import org.eltech.ddm.associationrules.ItemSet;
import org.eltech.ddm.associationrules.ItemSets;
import org.eltech.ddm.associationrules.apriori.dhp.DHPMiningModel;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.algorithms.Step;
import org.eltech.ddm.miningcore.miningfunctionsettings.EMiningFunctionSettings;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class GetLargeItemSetsStep extends Step{
	
	private final double minSupport;

	public GetLargeItemSetsStep(EMiningFunctionSettings settings) throws MiningException {
		super(settings);
		minSupport = ((AssociationRulesFunctionSettings)settings).getMinSupport();
	}

	@Override
	protected EMiningModel execute(MiningInputStream inputData, EMiningModel model) throws MiningException {
		DHPMiningModel modelA = (DHPMiningModel) model;
		Map<List<String>, Integer> map = modelA.getItemSetsHashTable().get(modelA.getCurrentLargeItemSets() + 1);
		ItemSets currItemsetList = null;
		if(modelA.getLargeItemSetsList().size() <= (modelA.getCurrentLargeItemSets())){
			currItemsetList = new ItemSets();
			modelA.getLargeItemSetsList().add(currItemsetList);
		} else
			currItemsetList = modelA.getLargeItemSetsList().get(modelA.getCurrentLargeItemSets());
		for (List<String> key : map.keySet()) {
			if ((map.get(key) / (double)modelA.getTransactionCount()) >= minSupport) {
				ItemSet itemSet = new ItemSet(key);
				itemSet.setSupportCount(map.get(key));
				currItemsetList.add(itemSet);
				
			}
		}
		return modelA;
	}

}
