package src.test.java.performanceTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import src.main.java.testDriver.TestPerformanceKibana;

public class LoadTests {
	
	TestPerformanceKibana mainTest;
	private final String BASE_URL = "http://localhost:5601/app/kibana#/dashboards";
	@Before 
	public void setup() {
		
		mainTest = new TestPerformanceKibana();
		
	}
	
	
	@Test
	public void test() {
		mainTest.loadKibanaPage(BASE_URL);
		
		
		long startTime = mainTest.goToDashBoard();
		mainTest.waitForDashBoardToFinishLoading();
		//mainTest.setDashBoardTimeFilter("Mar 1, 2020 @ 00:00:00.000","Mar 2, 2020 @ 00:00:00.000");
		mainTest.setFieldFilters(null);
		mainTest.waitForDataRenderingToFinish();
		long duration = System.currentTimeMillis() - startTime;
		System.out.println(duration);
	}
}
