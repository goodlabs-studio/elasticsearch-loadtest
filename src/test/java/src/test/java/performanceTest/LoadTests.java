package src.test.java.performanceTest;



import java.util.HashMap;
import java.util.Map;


import org.junit.Before;
import org.junit.Test;

import src.main.java.elasticsearch.ElasticSearchClientForUITesting;
import src.main.java.testDriver.TestPerformanceKibana;

public class LoadTests {
	
	TestPerformanceKibana mainTest;
	private final String BASE_URL = "http://localhost:5601/app/kibana#/dashboards";
	private ElasticSearchClientForUITesting esClient;
	
	@Before 
	public void setup() {
		
		esClient = ElasticSearchClientForUITesting.getEsClient("localhost", 9200, "http");
		mainTest = new TestPerformanceKibana();
		
	}

	public void mainTest(String startDataTime, String endDataTime, Map<String, String> filters, int numTests) {
		
		mainTest.loadKibanaPage(BASE_URL);
		//initial load screen should default to last 15 minutes, a timeframe with no data
		
		mainTest.goToDashBoard();
		mainTest.waitForDashBoardToFinishLoading();
		mainTest.showDateFilter();
		//load field filters
		mainTest.setFieldFilters(filters);
		
		long total=0;
		for (int i = 0; i < numTests;i++) {

			//navgiate to "empty" data frame to unload any existing data
			mainTest.setDashBoardTimeFilter("Mar 1, 1999 @ 00:00:00.000", 
						"Mar 1, 1999 @ 00:00:01.000");
			mainTest.clickOnUpdateButton();
			mainTest.waitForDashBoardToFinishLoading();
			
			
			//set to "actual" date range
			mainTest.setDashBoardTimeFilter(startDataTime, endDataTime);
			mainTest.waitForDashBoardToFinishLoading();
			
			//clear ES cache
			System.out.println("clearing es cache..");
			esClient.clearESCache();
			
			//load data
			long startTime = System.currentTimeMillis();
			mainTest.clickOnUpdateButton();
	
			mainTest.waitForDataRenderingToFinish();
			
			long duration = System.currentTimeMillis() - startTime;
			total = total + duration;
			System.out.println("Test number: "+i+" time taken:"+ duration);
		}
		System.out.println("avg time in ms: " + total/numTests);
	}
	
	@Test
	public void testSimple() {
	
		//load field filters
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("applications", "Application-12");
		h.put("localPort", "9310");
		
		mainTest("Mar 1, 2020 @ 00:00:00.000", "Mar 2, 2020 @ 00:00:00.000",h, 10);
	}
	
	
	
}
