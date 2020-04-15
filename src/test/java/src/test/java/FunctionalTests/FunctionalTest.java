package src.test.java.FunctionalTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import src.main.java.testDriver.TestPerformanceKibana;

public class FunctionalTest {
	TestPerformanceKibana mainTest;
	private final String BASE_URL = "http://localhost:5601/app/kibana#/dashboards";
	@Before 
	public void setup() {
		
		mainTest = new TestPerformanceKibana();
		
	}
	
	
	
	@Test
	public void testFilterField() {
		mainTest.loadKibanaPage(BASE_URL);
		mainTest.goToDashBoard();
		

		mainTest.waitForDashBoardToFinishLoading();
		mainTest.setFieldFilters(null);


	}

}
