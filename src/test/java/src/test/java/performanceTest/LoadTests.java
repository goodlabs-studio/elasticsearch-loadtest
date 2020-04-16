package src.test.java.performanceTest;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
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
	

	
	
	
	public void mainTest(String startDataTime, String endDataTime, Map<String, String> filters) {
		
		mainTest.loadKibanaPage(BASE_URL);
		//initial load screen should default to last 15 minutes, a timeframe with no data
		
		mainTest.goToDashBoard();
		mainTest.waitForDashBoardToFinishLoading();
		
		
		//navgiate to "empty" data frame
		mainTest.setDashBoardTimeFilter("Mar 1, 1999 @ 00:00:00.000", 
					"Mar 1, 1999 @ 00:00:01.000");
		mainTest.clickOnUpdateButton();
		mainTest.waitForDashBoardToFinishLoading();
		//load field filters
		
		mainTest.setFieldFilters(filters);
		
		//set to "actual" date range
		
		
		mainTest.setDashBoardTimeFilter(startDataTime, endDataTime);
		mainTest.waitForDashBoardToFinishLoading();
		long startTime = System.currentTimeMillis();
		mainTest.clickOnUpdateButton();

		mainTest.waitForDataRenderingToFinish();
		
		long duration = System.currentTimeMillis() - startTime;
		System.out.println(duration);
	}
	
	@Test
	public void testSimple() {
	
		//load field filters
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("applications", "Application-12");
		h.put("localPort", "9310");
		
		mainTest("Mar 1, 2020 @ 00:00:00.000", "Mar 2, 2020 @ 00:00:00.000",h);
	}
	
	
	
}
