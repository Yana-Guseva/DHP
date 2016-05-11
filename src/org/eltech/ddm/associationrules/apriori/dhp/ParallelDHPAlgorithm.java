package org.eltech.ddm.associationrules.apriori.dhp;

import org.eltech.ddm.associationrules.AssociationRulesFunctionSettings;
import org.eltech.ddm.associationrules.apriori.dhp.steps.CreateHashTable;
import org.eltech.ddm.associationrules.apriori.dhp.steps.GetLargeItemSetsStep;
import org.eltech.ddm.associationrules.apriori.dhp.steps.IsThereCurrentTransaction;
import org.eltech.ddm.associationrules.apriori.dhp.steps.PruneTransactionListStep;
import org.eltech.ddm.associationrules.apriori.dhp.steps.PruningStep;
import org.eltech.ddm.associationrules.apriori.dhp.steps.SetTransactionCountStep;
import org.eltech.ddm.associationrules.apriori.steps.BuildTransactionStep;
import org.eltech.ddm.associationrules.apriori.steps.Calculate1ItemSetSupportStep;
import org.eltech.ddm.associationrules.apriori.steps.CreateLarge1ItemSetStep;
import org.eltech.ddm.associationrules.apriori.steps.GenerateAssosiationRuleStep;
import org.eltech.ddm.associationrules.apriori.steps.KLargeItemSetsCycleStep;
import org.eltech.ddm.associationrules.apriori.steps.LargeItemSetItemsCycleStep;
import org.eltech.ddm.associationrules.apriori.steps.LargeItemSetListsCycleStep;
import org.eltech.ddm.associationrules.apriori.steps.TransactionItemsCycleStep;
import org.eltech.ddm.associationrules.steps.TransactionsCycleStep;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.algorithms.MiningAlgorithm;
import org.eltech.ddm.miningcore.algorithms.ParallelByData;
import org.eltech.ddm.miningcore.algorithms.StepExecuteTimingListner;
import org.eltech.ddm.miningcore.algorithms.StepSequence;
import org.eltech.ddm.miningcore.algorithms.VectorsCycleStep;
import org.eltech.ddm.miningcore.miningfunctionsettings.EMiningFunctionSettings;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class ParallelDHPAlgorithm extends MiningAlgorithm{

	public ParallelDHPAlgorithm(EMiningFunctionSettings miningSettings) throws MiningException {
		super(miningSettings);
	}

	@Override
	public EMiningModel createModel(MiningInputStream inputStream) throws MiningException {
		EMiningModel resultModel = new DHPMiningModel((AssociationRulesFunctionSettings) miningSettings);
		return resultModel;
	}

	@Override
	protected void initSteps() throws MiningException {
		VectorsCycleStep vcs = new VectorsCycleStep(miningSettings, new BuildTransactionStep(miningSettings), 
				new SetTransactionCountStep(miningSettings));
		vcs.addListenerExecute(new StepExecuteTimingListner());

		TransactionsCycleStep tcs = new TransactionsCycleStep(miningSettings,
				new TransactionItemsCycleStep(miningSettings, new Calculate1ItemSetSupportStep(miningSettings),
						new CreateLarge1ItemSetStep(miningSettings)),
				new CreateHashTable(miningSettings));
		tcs.addListenerExecute(new StepExecuteTimingListner());
		
		LargeItemSetListsCycleStep lislcs = new LargeItemSetListsCycleStep(miningSettings, 
				new ParallelByData(miningSettings, new StepSequence(miningSettings, 
				new PruningStep(miningSettings),
//				new GetLargeItemSetsStep(miningSettings),
//				new K_1LargeItemSetsCycleStep(miningSettings, new K_1LargeItemSetsFromCurrentCycleStep(miningSettings,
//						new CreateKItemSetCandidateStepUsingHashTable(miningSettings))),
				new TransactionsCycleStep(miningSettings, 
					new PruneTransactionListStep(miningSettings),
						new IsThereCurrentTransaction(miningSettings, new CreateHashTable(miningSettings))))));
		lislcs.addListenerExecute(new StepExecuteTimingListner());

		LargeItemSetListsCycleStep lislcs2 = new LargeItemSetListsCycleStep(miningSettings, new KLargeItemSetsCycleStep(
				miningSettings,
				new LargeItemSetItemsCycleStep(miningSettings, new GenerateAssosiationRuleStep(miningSettings))));
		lislcs2.addListenerExecute(new StepExecuteTimingListner());

		steps = new StepSequence(miningSettings, vcs, tcs, lislcs, lislcs2);

		steps.addListenerExecute(new StepExecuteTimingListner());		
	}

}
