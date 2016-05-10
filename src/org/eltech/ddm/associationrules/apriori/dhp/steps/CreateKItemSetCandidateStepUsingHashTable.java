package org.eltech.ddm.associationrules.apriori.dhp.steps;

import java.util.List;
import java.util.Map;

import org.eltech.ddm.associationrules.ItemSet;
import org.eltech.ddm.associationrules.ItemSets;
import org.eltech.ddm.associationrules.apriori.dhp.DHPMiningModel;
import org.eltech.ddm.associationrules.apriori.steps.CreateKItemSetCandidateStep;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.miningfunctionsettings.EMiningFunctionSettings;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class CreateKItemSetCandidateStepUsingHashTable extends CreateKItemSetCandidateStep{

	public CreateKItemSetCandidateStepUsingHashTable(EMiningFunctionSettings settings) throws MiningException {
		super(settings);
	}

	@Override
	protected EMiningModel execute(MiningInputStream inputData, EMiningModel model) throws MiningException {
		DHPMiningModel modelA = (DHPMiningModel) model;
//		System.out.println("modelA " + modelA.getItemSetsHashTable());
		Map<List<String>, Integer> map = modelA.getItemSetsHashTable().get(modelA.getCurrentLargeItemSets() + 1);
		ItemSets prevItemsetList = modelA.getLargeItemSetsList().get(modelA.getCurrentLargeItemSets() - 1);
		ItemSets currItemsetList = null;
		if(modelA.getLargeItemSetsList().size() <= (modelA.getCurrentLargeItemSets())){
			currItemsetList = new ItemSets();
			modelA.getLargeItemSetsList().add(currItemsetList);
		} else
			currItemsetList = modelA.getLargeItemSetsList().get(modelA.getCurrentLargeItemSets());

		ItemSet itemSet = prevItemsetList.get(modelA.getCurrentItemSet());
		ItemSet itemSet2 = prevItemsetList.get(modelA.getCurrentItemSet2());

		//System.out.println("Thread: " + Thread.currentThread().getName() + " itemSet=" + itemSet + " itemSet2=" + itemSet2);

		ItemSet newItemSet = union(itemSet, itemSet2);
		modelA.setCurrentCandidate(-1);
		
//		System.out.println(map);
//		System.out.println("itemIDList " + newItemSet.getItemIDList());
		if((newItemSet.getItemIDList().size() == itemSet.getItemIDList().size() + 1)) {
			List<String> key = newItemSet.getItemIDList();
			key.sort(null);
			if (map.containsKey(key)) {
				if(!currItemsetList.contains(newItemSet)) {
					newItemSet.setSupportCount(map.get(key));
					currItemsetList.add(newItemSet);
				}
				int currentCandidate = currItemsetList.indexOf(newItemSet);
				modelA.setCurrentCandidate(currentCandidate);
			}
		}
		return modelA;
	}

}
