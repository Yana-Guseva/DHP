package org.eltech.ddm.associationrules.apriori.dhp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eltech.ddm.associationrules.AssociationRulesFunctionSettings;
import org.eltech.ddm.associationrules.AssociationRulesMiningModel;
import org.eltech.ddm.associationrules.ItemSets;
import org.eltech.ddm.associationrules.Transaction;
import org.eltech.ddm.associationrules.TransactionList;
import org.eltech.ddm.associationrules.apriori.AprioriMiningModel;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class DHPMiningModel extends AprioriMiningModel{
	
	protected Map<Integer, Map<List<String>, Integer>> itemSetsHashTable = new HashMap<>();
	
	private boolean isTransactionPruned = false;
	
	public boolean isTransactionPruned() {
		return isTransactionPruned;
	}

	public void setTransactionPruned(boolean isTransactionpruned) {
		this.isTransactionPruned = isTransactionpruned;
	}

	private int transactionCount = 0;
	
	public int getTransactionCount() {
		return transactionCount;
	}

	public void setTransactionCount(int transactionCount) {
		this.transactionCount = transactionCount;
	}

	public DHPMiningModel(AssociationRulesFunctionSettings settings) throws MiningException {
		super(settings);
	}

	public Map<Integer, Map<List<String>, Integer>> getItemSetsHashTable() {
		return itemSetsHashTable;
	}

	public void setItemSetsHashTree(Map<Integer, Map<List<String>, Integer>> itemSetsHashTree) {
		this.itemSetsHashTable = itemSetsHashTree;
	}

	@Override
	public void join(List<EMiningModel> joinModels) throws MiningException {
//		super.join(joinModels);
		int index = getCurrentLargeItemSets() + 2;
		for(EMiningModel mm: joinModels){
			if(mm == this)
				continue;
			DHPMiningModel dhpmm = (DHPMiningModel) mm;
//			System.out.println("index " + index);
			Map<List<String>, Integer> map = dhpmm.getItemSetsHashTable().get(index);
//			System.out.println("map " + map);
			if (map != null) {
				if (!itemSetsHashTable.containsKey(index)) {
					itemSetsHashTable.put(index, new HashMap<List<String>, Integer>());
				}
				Map<List<String>, Integer> commonMap = itemSetsHashTable.get(index);
				for (List<String> key : map.keySet()) {
					key.sort(null);
					if (commonMap.containsKey(key)) {
						Integer count = commonMap.get(key) + map.get(key);
						commonMap.put(key, count);
					} else {
						commonMap.put(key, map.get(key));
					}
				}
			}
			
			ItemSets curList = dhpmm.getLargeItemSetsList().get(getCurrentLargeItemSets());
//			System.out.println(dhpmm.getLargeItemSetsList());
//			System.out.println("curList " + curList);
			if (curList.size() > 0) {
//				System.out.println("here");
				int k = curList.size();
				if(getCurrentLargeItemSets() >= largeItemSetsList.size())
					largeItemSetsList.add(new ItemSets());
				largeItemSetsList.get(getCurrentLargeItemSets()).addAll(curList);
			}
		}
//		System.out.println("join " + largeItemSetsList);
	}
	
	@Override
	public ArrayList<EMiningModel> split(int handlerCount)
			throws MiningException {
		ArrayList<EMiningModel> models = super.split(handlerCount);
		int index = getCurrentLargeItemSets() + 1;
//		System.out.println("index " + index);
		Map<List<String>, Integer> map = itemSetsHashTable.get(index);
//		System.out.println("split " + map);

		
		if (map != null && map.size() > 0) {
			int iModel = -1;
			if (map.size() < handlerCount) {
				for (EMiningModel model : models) {
					((DHPMiningModel) model).getItemSetsHashTable().get(index).clear(); 
				}
				for (List<String> key : map.keySet()) {
					iModel++;
					((DHPMiningModel) models.get(iModel)).getItemSetsHashTable().get(index).put(key, map.get(key));
				}
			} else {
				int count = Math.round(map.size() / handlerCount) + 1;
				int i = count;
				for (List<String> key : map.keySet()) {
					if (i == count) {
						iModel++;
						i = 0;
						((DHPMiningModel) models.get(iModel)).getItemSetsHashTable().get(index).clear(); 
					}
					((DHPMiningModel) models.get(iModel)).getItemSetsHashTable().get(index).put(key, map.get(key));
					i++;
				}
			}
//			int count = (map.size() / handlerCount) % 1 == 0 ? map.size() / handlerCount : Math.round(map.size() / handlerCount) + 1;
			
//			System.out.println("0 " + ((DHPMiningModel) models.get(0)).getItemSetsHashTable().get(index));
//			System.out.println("1 " + ((DHPMiningModel) models.get(1)).getItemSetsHashTable().get(index));
		}

		return models;
	}
	
	public Object clone() {
		DHPMiningModel o = null;
		o = (DHPMiningModel)super.clone();

		if(itemSetsHashTable != null){
			o.itemSetsHashTable = new HashMap<Integer, Map<List<String>, Integer>>();
			
			for (Integer i : itemSetsHashTable.keySet()) {
				Map<List<String>, Integer> map = itemSetsHashTable.get(i);
				Map<List<String>, Integer> copiedMap = new HashMap<List<String>, Integer>();
				for (List<String> key : map.keySet()) {
					copiedMap.put(new ArrayList<String>(key), new Integer(map.get(key)));
				}
				o.itemSetsHashTable.put(i, copiedMap);
			}
		}

		return o;
	}
}
