package org.eltech.ddm.associationrules.apriori.dhp.steps;

import java.util.Iterator;
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

public class PruningStep extends Step{
	private final double minSupport;

	public PruningStep(EMiningFunctionSettings settings) throws MiningException {
		super(settings);
		minSupport = ((AssociationRulesFunctionSettings)settings).getMinSupport();
	}

	@Override
	protected EMiningModel execute(MiningInputStream inputData, EMiningModel model) throws MiningException {
		DHPMiningModel modelA = (DHPMiningModel) model;
		Map<List<String>, Integer> map = modelA.getItemSetsHashTable().get(modelA.getCurrentLargeItemSets() + 1);
//		System.out.println(Thread.currentThread().getName() + " " + map);
		ItemSets currItemsetList = null;
		if(modelA.getLargeItemSetsList().size() <= (modelA.getCurrentLargeItemSets())){
			currItemsetList = new ItemSets();
			modelA.getLargeItemSetsList().add(currItemsetList);
		} else
			currItemsetList = modelA.getLargeItemSetsList().get(modelA.getCurrentLargeItemSets());
		if (map != null) {
			Iterator it = map.keySet().iterator();
			while (it.hasNext()){
				List<String> items = (List<String>) it.next();
				if(((double) map.get(items) / (double)modelA.getTransactionCount()) < minSupport){
					it.remove();
				} else {
					ItemSet itemSet = new ItemSet(items);
					itemSet.setSupportCount(map.get(items));
					currItemsetList.add(itemSet);
				}
			}
		}
//		System.out.println("large " + Thread.currentThread().getName() + " " + modelA.getLargeItemSetsList().get(modelA.getCurrentLargeItemSets()));
//		System.out.println(Thread.currentThread().getName() + " " + map);
		return modelA;
	}

}
