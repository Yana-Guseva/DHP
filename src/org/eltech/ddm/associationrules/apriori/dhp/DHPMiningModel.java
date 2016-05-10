package org.eltech.ddm.associationrules.apriori.dhp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eltech.ddm.associationrules.AssociationRulesFunctionSettings;
import org.eltech.ddm.associationrules.ItemSets;
import org.eltech.ddm.associationrules.Transaction;
import org.eltech.ddm.associationrules.TransactionList;
import org.eltech.ddm.associationrules.apriori.AprioriMiningModel;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class DHPMiningModel extends AprioriMiningModel{
	
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

	protected Map<Integer, Map<List<String>, Integer>> itemSetsHashTable = new HashMap<>();

	public Map<Integer, Map<List<String>, Integer>> getItemSetsHashTable() {
		return itemSetsHashTable;
	}

	public void setItemSetsHashTree(Map<Integer, Map<List<String>, Integer>> itemSetsHashTree) {
		this.itemSetsHashTable = itemSetsHashTree;
	}

	@Override
	public void join(List<EMiningModel> joinModels) throws MiningException {
		super.join(joinModels);
		int index = getCurrentLargeItemSets() + 2;
		for(EMiningModel mm: joinModels){
			if(mm == this)
				continue;
			DHPMiningModel dhpmm = (DHPMiningModel) mm;
			Map<List<String>, Integer> map = dhpmm.getItemSetsHashTable().get(index);
			if (map != null) {
				if (!itemSetsHashTable.containsKey(index)) {
					itemSetsHashTable.put(index, new HashMap<List<String>, Integer>());
				}
				Map<List<String>, Integer> commonMap = itemSetsHashTable.get(index);
				for (List<String> key : map.keySet()) {
					if (commonMap.containsKey(key)) {
						Integer count = commonMap.get(key) + map.get(key);
						commonMap.put(key, count);
					} else {
						commonMap.put(key, map.get(key));
					}
				}
			}
			
			List<ItemSets> curList = dhpmm.getLargeItemSetsList();
			if (curList != null) {
				int k = curList.size();
				if(k >= largeItemSetsList.size())
					largeItemSetsList.add(new ItemSets());
				largeItemSetsList.get(k - 1).addAll(curList.get(k -1));
			}
		}
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
