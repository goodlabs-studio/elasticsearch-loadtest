package src.test.java.performanceTest;

import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import src.main.java.elasticsearch.ElasticSearchClientForUITesting;
import src.main.java.testDriver.NetworkDashBoardPageDriver;

public class LoadTestsChromeNetworkDashBoard {
	
	private NetworkDashBoardPageDriver mainTest;
	private String kibanaUrl;
	private String elasticSearchUrl;
	
	private final String NAME_OF_NETWORK_DASHBOARD="Application Raw Network Metric Dashboard";
	private final String CHROME_LOCATION = "C:\\Users\\Richard\\Documents\\Development\\Selenium\\KibanaDataTest\\src\\main\\resources\\chromedriver_win32\\chromedriver.exe";
	private ElasticSearchClientForUITesting esClient;
	private ChromeOptions chromeOptions;

	final static Logger logger = LogManager.getLogger(LoadTestsChromeNetworkDashBoard.class);

	
	@BeforeEach
	public void setup() {
		
		kibanaUrl = System.getProperty("kibanaUrl","http://localhost:5601");		
		elasticSearchUrl = System.getProperty("elasticSearchUrl", "http://localhost:9200");
		esClient = ElasticSearchClientForUITesting.getEsClient(elasticSearchUrl);
		
		boolean headlessMode = Boolean.valueOf(System.getProperty("headlessMode", "false"));
		List<String> options = new ArrayList<String>();
		if (headlessMode) {
			options.add("--headless");
		}
		
		options.add("--window-size=1920,1080");
		
		this.chromeOptions = new ChromeOptions();
		chromeOptions.addArguments(options);
		System.setProperty("webdriver.chrome.driver", CHROME_LOCATION);

	}
	


	public long testScenerio
		(String startDataTime, 
				String endDataTime, 
				Map<String, String> filters, 
				String dashBoardName,
				int numTests, long timeoutInSeconds) {
		
		WebDriver driver = new ChromeDriver(chromeOptions);
		driver.manage().timeouts().implicitlyWait(timeoutInSeconds, TimeUnit.SECONDS);
		
		mainTest = new NetworkDashBoardPageDriver(driver);
		mainTest.loadKibanaPage(kibanaUrl);
		//initial load screen should default to last 15 minutes, a timeframe with no data
		
		mainTest.goToDashBoard(dashBoardName);
		mainTest.waitForDashBoardToFinishLoading(Duration.ofSeconds(40));
		mainTest.showDateFilter();
		//load field filters
		mainTest.setFieldFilters(filters);
		
		long total=0;
		for (int i = 1; i <= numTests;i++) {

			logger.info("starting test run...");
			//navigate to "empty" data frame to unload any existing data
			//this should not take very long
			mainTest.setDashBoardTimeFilter("Mar 1, 1999 @ 00:00:00.000", 
						"Mar 1, 1999 @ 00:00:01.000");
			mainTest.clickOnUpdateButton();
			mainTest.waitForDashBoardToFinishLoading(Duration.ofSeconds(10));
			
			
			//set to "actual" date range
			mainTest.setDashBoardTimeFilter(startDataTime, endDataTime);
			mainTest.waitForDashBoardToFinishLoading(Duration.ofSeconds(10));
			
			//clear ES cache
			logger.info("clearing es cache..");
			logger.info(esClient.clearESCache().getStatus().toString());
			
			//load data
			long startTime = System.currentTimeMillis();
			logger.info("setting date range to {} to {}", startDataTime, endDataTime);
			
			//load data
			mainTest.clickOnUpdateButton();
			
			try {
				mainTest.waitForDataRenderingToFinish(Duration.ofSeconds(timeoutInSeconds));
			} catch (TimeoutException e) {
				
				
				mainTest.getPageScreenshot("timeout-time:"+Time.from(Instant.now())+".png");
			}
			
			long duration = System.currentTimeMillis() - startTime;
			total = total + duration;
			logger.info("Test number: {} time taken: {} {}", i, duration, "miliseconds");
		}
		
		long avgTime = total/numTests;
		logger.info("Average Time Taken {} {}", avgTime,"miliseconds");
		mainTest.closeBrowser();
		return avgTime;

	}
	

	public void testTimeRangeForFilters
	(HashMap<String, String> filters, 
			String dashBoardName, int numEachDateRange, List<Long> testResultCriterias) {
		
		testScenerio("Mar 1, 2020 @ 00:00:00.000", "Mar 2, 2020 @ 00:00:00.000", filters, dashBoardName, numEachDateRange, 1200);
		testScenerio("Mar 1, 2020 @ 00:00:00.000", "Mar 4, 2020 @ 00:00:00.000", filters, dashBoardName, numEachDateRange, 1200);
		testScenerio("Mar 1, 2020 @ 00:00:00.000", "Mar 11, 2020 @ 00:00:00.000", filters, dashBoardName, numEachDateRange, 1200);
		testScenerio("Mar 1, 2020 @ 00:00:00.000", "Mar 31, 2020 @ 00:00:00.000", filters, dashBoardName, numEachDateRange, 1200);
	}
	
	@Test
	public void testApplicationPortFilters() {
	
		//load field filters
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("applications", "Application-9");
		h.put("localPort", "25011");
		
		
		testTimeRangeForFilters(h, NAME_OF_NETWORK_DASHBOARD, 3, null);
	}
	
	@Test
	public void measureApplicationFilter() {
	
		//load field filters
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("applications", "Application-12");

		testTimeRangeForFilters(h, NAME_OF_NETWORK_DASHBOARD, 3, null);
	}
	
	@Test
	public void measureCommandFilter() {
	
		//load field filters
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("command", "/usr/sbin/sshd -D");

		testTimeRangeForFilters(h, NAME_OF_NETWORK_DASHBOARD, 3, null);
	}
	
	@Test
	public void measureCommandAndApplicationFilter() {
		
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("command", "/usr/sbin/sshd -D");
		h.put("applications", "Application-12");

		testTimeRangeForFilters(h, NAME_OF_NETWORK_DASHBOARD, 3, null);
	}

	@Test
	public void measureCommandAndApplicationFilterAndPort() {
		
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("command", "/usr/sbin/sshd -D");
		h.put("applications", "Application-12");
		h.put("localPort", "44234");

		testTimeRangeForFilters(h, NAME_OF_NETWORK_DASHBOARD, 3, null);
	}

	
	@Test
	public void measureForeignAddress() {
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("foreignAddress", "196.198.19.5");


		testTimeRangeForFilters(h, NAME_OF_NETWORK_DASHBOARD, 3, null);
		
	}
	
	@Test
	public void measureForeignAddressAndCommand() {
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("foreignAddress", "196.198.19.5");
		h.put("command", "curl -sk https://yahoo.com/ -o /dev/null");

		testTimeRangeForFilters(h, NAME_OF_NETWORK_DASHBOARD, 3, null);
		
	}
	
	
}
