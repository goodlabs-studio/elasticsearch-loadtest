package src.test.java.performanceTest;


import static org.junit.Assert.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import main.src.java.ESQueryBuilder;
import main.src.java.KibanaNetworkDashBoardPage;

public class TestPerformanceKibana {

	private WebDriver driver;
	private ESQueryBuilder esQueryBuilder = new ESQueryBuilder();
	private final String CHROME_LOCATION = "C:\\Users\\Richard\\Documents\\Development\\Selenium\\KibanaDataTest\\src\\main\\resources\\chromedriver_win32\\chromedriver.exe";
	private final String BASE_URL = "http://localhost:5601/app/kibana#/dashboards";

	KibanaNetworkDashBoardPage page = new KibanaNetworkDashBoardPage();
	
	@Before
	public void driver () {
		 
		System.setProperty("webdriver.chrome.driver", CHROME_LOCATION);
        driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.SECONDS);
	}

	private void loadKibanaPage(String url) {
		
        driver.get(url);

	}
	

	//go to dashboard, records the time when "user" clicked dashboard button
	private long goToDashBoard() {
		WebDriverWait wait = new WebDriverWait(driver,500);
		wait.
			until(ExpectedConditions.
					visibilityOfElementLocated
					(By.linkText("Application Raw Network Metric Dashboard")));
		
		WebElement dashboardButton 
			= driver.findElement(By.ByLinkText.linkText("Application Raw Network Metric Dashboard"));

		dashboardButton.click();
		return System.currentTimeMillis();

	}

	//wait for initial dashboard to finish loading, default is 15 min of data
	public long waitForDashBoardToFinishLoading() {

		waitForDataRenderingToFinish();

		return System.currentTimeMillis();
	}
	

	
	public void waitForDataRenderingToFinish() {
		
		WebDriverWait waitForDataComplete = new WebDriverWait(driver, 100);
		
		waitForDataComplete.
			until( page.buildExpectedConditionsForDataRenderingDone());
	}
	
	public void setFieldFilters (List<String> filters) {
		
		WebElement addFilterButton = 
				driver.findElement(page.getAddFilterButton());
		addFilterButton.click();
		
		WebElement addQueryDSLButton = 
				driver.findElement(page.getQueryDSLButton());
		addQueryDSLButton.click();

		WebElement codeBox = 
				driver.findElement(page.getQueryDSLCodeBox());

		codeBox.clear();

		//Kibana does annoying auto-complete with bracets on queries which breaks
		//direct insertion, so we have to paste our query

		pasteIntoTextBox (esQueryBuilder.buildQuery(null,null), codeBox, driver);
		//codeBox.sendKeys(esQueryBuilder.buildQuery(startTime, endTime));
	}
	
	//set time filters
	private void setDashBoardTimeFilter(String startTime, String endTime) {
		

		WebDriverWait wait = new WebDriverWait(driver,500);
		
		WebElement showDatesButton = 
				driver.findElement(page.getShowDatesButton());
		showDatesButton.click();
		
		//navigate to start date text box
		WebElement startDateButton = 
				driver.findElement(page.getStartDateButton());
		startDateButton.click();
		
		WebElement absoluteButton = 
				driver.findElement(page.timeSettingBox.getAbsoluteButton());
		absoluteButton.click();
		
		WebElement startDateBox =
				driver.findElement(page.timeSettingBox.byGetStartDateBox());

		//set for startdate
		setDateInDateBox(startDateBox, startTime, wait);

	}
	
	private void setDateInDateBox (WebElement dateBox, String date, WebDriverWait wait) {
		
		wait.until(ExpectedConditions.elementToBeClickable(dateBox)).click();
		
		//getting around annoying textbox becoming uninteractable upon using .clear() method
		dateBox.sendKeys(Keys.CONTROL + "a");
		dateBox.sendKeys(Keys.DELETE);
		dateBox.sendKeys(date);
	}
	
	private void pasteIntoTextBox (String text, WebElement textBox, WebDriver driver) {
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection selection = new StringSelection(text);
		clipboard.setContents(selection, selection);
		textBox.sendKeys(Keys.chord(Keys.LEFT_CONTROL, "v"));

	}
	
	@Test
	public void test() {
		loadKibanaPage(BASE_URL);
		
		
		long startTime = goToDashBoard();
		waitForDashBoardToFinishLoading();
		setDashBoardTimeFilter("Mar 1, 2020 @ 00:00:00.000","Mar 2, 2020 @ 00:00:00.000");
		//setFieldFilters("Mar 1, 2020 @ 00:00:00.000","Mar 2, 2020 @ 00:00:00.000");
		waitForDataRenderingToFinish();
		long duration = System.currentTimeMillis() - startTime;
		System.out.println(duration);
	}

}
