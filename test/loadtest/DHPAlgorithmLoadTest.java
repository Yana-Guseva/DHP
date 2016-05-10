package loadtest;

import org.eltech.ddm.associationrules.apriori.dhp.DHPAlgorithm;
import org.eltech.ddm.associationrules.apriori.dhp.DHPMiningModel;
import org.eltech.ddm.miningcore.MiningException;
import org.junit.Before;
import org.junit.Test;

public class DHPAlgorithmLoadTest extends DHPLoadTest{
	@Before
	public void setUp() throws Exception {
	}

	
	@Test
	public void testHPAAlgoritm() throws MiningException {
		System.out.println("----- DHPAlgoritm -------");
		
//		for(int i=0; i < dataSets.length; i++){
			setSettings(4);
		
			DHPAlgorithm algorithm = new DHPAlgorithm(miningSettings);
			System.out.println("Start algorithm");
			miningModel = (DHPMiningModel) algorithm.buildModel(inputData);

			System.out.println("Finish algorithm. Calculation time: " + algorithm.getTimeSpentToBuildModel());
			
			verifyModel();
			
		}
//	}
}
