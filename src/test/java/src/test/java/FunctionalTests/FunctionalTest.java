package src.test.java.FunctionalTests;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import src.main.java.testDriver.NetworkDashBoardPageDriver;

public class FunctionalTest {
	NetworkDashBoardPageDriver mainTest;
	private final String BASE_URL = "http://localhost:5601/app/kibana#/dashboards";
	@Before 
	public void setup() {
		
		mainTest = new NetworkDashBoardPageDriver();
		
	}
	
	
	
	@Test
	public void testFilterField() {
		mainTest.loadKibanaPage(BASE_URL);
		mainTest.goToDashBoard();
		

		mainTest.waitForDashBoardToFinishLoading();
		
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("applications", "Application-12");

		
		mainTest.setFieldFilters(h);

	}
	
	@Test
	public void testFilterFieldMultiple() {
		
		mainTest.loadKibanaPage(BASE_URL);
		mainTest.goToDashBoard();
		

		mainTest.waitForDashBoardToFinishLoading();
		
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("applications", "Application-12");
		h.put("localPort", "9310");
		
		mainTest.setFieldFilters(h);
	}

}
