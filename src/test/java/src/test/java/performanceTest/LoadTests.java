package src.test.java.performanceTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import src.main.java.elasticsearch.ElasticSearchClientForUITesting;
import src.main.java.testDriver.NetworkDashBoardPageDriver;

public class LoadTests {
	
	NetworkDashBoardPageDriver mainTest;
	private String kibanaUrl;
	private String elasticSearchUrl;
	
	private final String CHROME_LOCATION = "C:\\Users\\Richard\\Documents\\Development\\Selenium\\KibanaDataTest\\src\\main\\resources\\chromedriver_win32\\chromedriver.exe";
	private ElasticSearchClientForUITesting esClient;
	private List<String> options;
	
	@Before 
	public void setup() {

		kibanaUrl = System.getProperty("kibanaUrl","http://localhost:5601");		
		elasticSearchUrl = System.getProperty("elasticSearchUrl", "http://localhost:9200");
		esClient = ElasticSearchClientForUITesting.getEsClient(elasticSearchUrl);
		
		this.options = new ArrayList<String>();

	}
	


	public void testScenerio(String startDataTime, String endDataTime, Map<String, String> filters, int numTests) {
		
		mainTest = new NetworkDashBoardPageDriver(options, CHROME_LOCATION);
		mainTest.loadKibanaPage(kibanaUrl);
		//initial load screen should default to last 15 minutes, a timeframe with no data
		
		mainTest.goToDashBoard();
		mainTest.waitForDashBoardToFinishLoading();
		mainTest.showDateFilter();
		//load field filters
		mainTest.setFieldFilters(filters);
		
		long total=0;
		for (int i = 1; i <= numTests;i++) {

			//navigate to "empty" data frame to unload any existing data
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
		mainTest.closeChrome();
	}
	

	public void testTimeRangeForFilters(HashMap<String, String> filters, int numEachDateRange) {
		
		testScenerio("Mar 1, 2020 @ 00:00:00.000", "Mar 2, 2020 @ 00:00:00.000", filters, numEachDateRange);
		testScenerio("Mar 1, 2020 @ 00:00:00.000", "Mar 4, 2020 @ 00:00:00.000", filters, numEachDateRange);
		testScenerio("Mar 1, 2020 @ 00:00:00.000", "Mar 11, 2020 @ 00:00:00.000", filters, numEachDateRange);
		testScenerio("Mar 1, 2020 @ 00:00:00.000", "Mar 31, 2020 @ 00:00:00.000", filters, numEachDateRange);
	}
	
	@Test
	public void testApplicationPortFilters() {
	
		//load field filters
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("applications", "Application-9");
		h.put("localPort", "25011");
		
		testScenerio("Mar 1, 2020 @ 00:00:00.000", "Mar 2, 2020 @ 00:00:00.000",h, 3);
	}
	
	@Test
	public void measureApplicationFilter() {
	
		//load field filters
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("applications", "Application-12");

		testTimeRangeForFilters(h, 3);
	}
	
	@Test
	public void measureCommandFilter() {
	
		//load field filters
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("command", "/usr/sbin/sshd -D");

		testTimeRangeForFilters(h, 3);
	}
	
	@Test
	public void measureCommandAndApplicationFilter() {
		
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("command", "/usr/sbin/sshd -D");
		h.put("applications", "Application-12");

		testTimeRangeForFilters(h, 3);
	}

	@Test
	public void measureCommandAndApplicationFilterAndPort() {
		
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("command", "/usr/sbin/sshd -D");
		h.put("applications", "Application-12");
		h.put("localPort", "44234");

		testTimeRangeForFilters(h, 3);
	}

	
	@Test
	public void measureForeignAddress() {
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("foreignAddress", "196.198.19.5");


		testTimeRangeForFilters(h, 3);
		
	}
	
	@Test
	public void measureForeignAddressAndCommand() {
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("foreignAddress", "196.198.19.5");
		h.put("command", "curl -sk https://yahoo.com/ -o /dev/null");

		testTimeRangeForFilters(h, 3);
		
	}
	
	
}
