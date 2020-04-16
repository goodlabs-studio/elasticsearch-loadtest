package src.test.java.performanceTest;



import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.utils.URIBuilder;
import org.junit.Before;
import org.junit.Test;

import src.main.java.elasticsearch.ElasticSearchClientForUITesting;
import src.main.java.testDriver.NetworkDashBoardPageDriver;

public class LoadTests {
	
	NetworkDashBoardPageDriver mainTest;
	private String kibanaDashBoardListUrl;
	
	private ElasticSearchClientForUITesting esClient;
	
	@Before 
	public void setup() {

		setKibanaDashboardURL(
							System.getProperty("kibanaURL","localhost"),
							Integer.valueOf(System.getProperty("kibanaPort", "5601")),
							System.getProperty("kibanaSchema", "http"));
		
		String esURL = System.getProperty("elasticSearchURL", "localhost");
		esClient = ElasticSearchClientForUITesting.getEsClient(esURL, 9200, "http");
		
		
	}
	
	private String setKibanaDashboardURL(String kibanaUrl, int port, String schema) {

		URIBuilder uriBuilder = new URIBuilder();
		String path = "app/kibana";
		uriBuilder.setHost(kibanaUrl);
		uriBuilder.setScheme(schema);
		uriBuilder.setPort(port);
		uriBuilder.setPath(path);
		uriBuilder.setFragment("dashboards");
		
		try {
			kibanaDashBoardListUrl = uriBuilder.build().toURL().toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return kibanaDashBoardListUrl;
	}
	
	@Test
	public void testSetKibanaURL() {
		
		System.out.println(setKibanaDashboardURL("localhost", 5601, "http"));

	}

	public void testScenerio(String startDataTime, String endDataTime, Map<String, String> filters, int numTests) {
		
		mainTest = new NetworkDashBoardPageDriver();
		mainTest.loadKibanaPage(kibanaDashBoardListUrl);
		//initial load screen should default to last 15 minutes, a timeframe with no data
		
		mainTest.goToDashBoard();
		mainTest.waitForDashBoardToFinishLoading();
		mainTest.showDateFilter();
		//load field filters
		mainTest.setFieldFilters(filters);
		
		long total=0;
		for (int i = 1; i <= numTests;i++) {

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
