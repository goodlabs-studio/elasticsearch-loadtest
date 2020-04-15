package src.test.java.performanceTest;

import static org.junit.Assert.*;

import java.util.HashMap;

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
		//initial load screen should default to last 15 minutes, a timeframe with no data
		
		mainTest.goToDashBoard();
		mainTest.waitForDashBoardToFinishLoading();
		
		//load field filters
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("applications", "Application-12");
		h.put("localPort", "9310");
		
		mainTest.setFieldFilters(h);
		
		//set date range
		mainTest.setDashBoardTimeFilter("Mar 1, 2020 @ 00:00:00.000","Mar 2, 2020 @ 00:00:00.000");
		
		long startTime = System.currentTimeMillis();
		mainTest.clickOnUpdateButton();

		mainTest.waitForDataRenderingToFinish();
		
		long duration = System.currentTimeMillis() - startTime;
		System.out.println(duration);
	}
	
	
	
}
