package loadtest;

import static org.junit.Assert.fail;

import org.eltech.ddm.associationrules.apriori.dhp.DHPMiningModel;
import org.eltech.ddm.associationrules.apriori.dhp.ParallelDHPAlgorithm;
import org.eltech.ddm.handlers.ExecutionSettings;
import org.eltech.ddm.handlers.thread.MultiThreadedExecutionEnvironment;
import org.eltech.ddm.inputdata.DataSplitType;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.miningfunctionsettings.DataProcessingStrategy;
import org.eltech.ddm.miningcore.miningfunctionsettings.MiningModelProcessingStrategy;
import org.eltech.ddm.miningcore.miningtask.EMiningBuildTask;
import org.junit.Test;

public class ParallelDHPAlgorithmLoadTest extends DHPLoadTest{
private final int NUMBER_HANDLERS = 2;
	
	@Test
	public void testParallelHPAAlgorithm() throws MiningException {
		System.out.println("----- ParallelDHPAlgorithm (" + NUMBER_HANDLERS + ")-------");
		
		try{
//			for(int i=0; i < dataSets.length; i++){
				setSettings(3);
			
				miningSettings.getAlgorithmSettings().setDataSplitType(DataSplitType.block);
				miningSettings.getAlgorithmSettings().setDataProcessingStrategy(DataProcessingStrategy.SeparatedDataSet);
				miningSettings.getAlgorithmSettings().setModelProcessingStrategy(MiningModelProcessingStrategy.SeparatedMiningModel);
			
				ExecutionSettings executionSettings = new ExecutionSettings();
//				executionSettings.setNumberHandlers(NUMBER_HANDLERS);
				miningSettings.getAlgorithmSettings().setNumberHandlers(NUMBER_HANDLERS);
				MultiThreadedExecutionEnvironment environment = new MultiThreadedExecutionEnvironment(executionSettings); 
				
				ParallelDHPAlgorithm algorithm = new ParallelDHPAlgorithm(miningSettings);
	
				EMiningBuildTask buildTask = new EMiningBuildTask();
				buildTask.setInputStream(inputData);
				buildTask.setMiningAlgorithm(algorithm); 
				buildTask.setMiningSettings(miningSettings);
				buildTask.setExecutionEnvironment(environment);
				System.out.println("Start algorithm");
				miningModel = (DHPMiningModel) buildTask.execute();
				System.out.println("Finish algorithm");
				
				verifyModel();
//			}
		}
		catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}
}
