
package org.eltech.ddm.associationrules.apriori.dhp.steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eltech.ddm.associationrules.ItemSet;
import org.eltech.ddm.associationrules.Transaction;
import org.eltech.ddm.associationrules.TransactionList;
import org.eltech.ddm.associationrules.apriori.dhp.DHPMiningModel;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.algorithms.Step;
import org.eltech.ddm.miningcore.miningfunctionsettings.EMiningFunctionSettings;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class CreateHashTable extends Step{

	public CreateHashTable(EMiningFunctionSettings settings) throws MiningException {
		super(settings);
	}

	@Override
	protected EMiningModel execute(MiningInputStream inputData, EMiningModel model) throws MiningException {
		DHPMiningModel modelA = (DHPMiningModel) model;
		TransactionList transactionList = modelA.getTransactionList();
//		System.out.println(Thread.currentThread().getName() + " " + transactionList);
		Transaction transaction = transactionList.get(modelA.getCurrentTransaction());
//		System.out.println("transaction " + transaction);
		int index = modelA.getCurrentLargeItemSets() + 2;
		if (!modelA.getItemSetsHashTable().containsKey(index)) {
			modelA.getItemSetsHashTable().put(index, new HashMap<List<String>, Integer>());
		}
//		System.out.println(Thread.currentThread().getName() + " " + "tran " + transaction);
		
		Map<List<String>, Integer> map = modelA.getItemSetsHashTable().get(index);
		getTransactionSubsets(map, transaction, index);
//		System.out.println(Thread.currentThread().getName() + " " + map);
		return modelA;
	}

	public void getTransactionSubsets(Map<List<String>, Integer> map, Transaction transaction, int k) {
		List<String> transactionItemIDList = transaction.getItemIDList();
		if (transactionItemIDList.size() < k) {
			return;
		}

		int indexes[] = new int[k];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = i;
		}

		boolean flag = true;
		while (flag) {
			List<String> elements = new ArrayList<String>();
			for (int i : indexes) {
				elements.add(transactionItemIDList.get(i));
			}
			elements.sort(null);
			if (map.containsKey(elements)) {
//				System.out.println("add " + elements);
				map.put(elements, map.get(elements) + 1);
			} else {
//				System.out.println("add " + elements);
				map.put(elements, 1);
			}
			int n = 1;
			while (flag) {
				indexes[indexes.length - n]++;
				if (indexes[indexes.length - n] < transactionItemIDList.size() - n + 1) {
					for (int h = indexes.length - n + 1; h < indexes.length; h++) {
						indexes[h] = indexes[h - 1] + 1;
					}
					n = 1;
					break;
				} else {
					n++;
					if (n == k + 1) {
						flag = false;
					}
				}
			}
		}
	}
}
