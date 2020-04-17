package src.main.java.testDriver;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import src.main.java.DashboardPage.KibanaNetworkDashBoardPage;

public class NetworkDashBoardPageDriver {

	private WebDriver driver;
	

	private TimeFilterDriver timeFilterDriver;
	private FieldFiltersDriver fieldFiltersDriver;
	
	KibanaNetworkDashBoardPage page = new KibanaNetworkDashBoardPage();
	

	public NetworkDashBoardPageDriver (List<String> chromeArguments, String chromeLocation) {
		 
		System.setProperty("webdriver.chrome.driver", chromeLocation);
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments(chromeArguments);
        driver = new ChromeDriver(options);

		driver.manage().timeouts().implicitlyWait(500, TimeUnit.SECONDS);
		
		fieldFiltersDriver = new FieldFiltersDriver(driver, page);
		timeFilterDriver = new TimeFilterDriver(driver, page);
	}

	public void loadKibanaPage(String url) {
		
		String dashBoardUrl =  getKibanaDashboardURL(url);
        driver.get(dashBoardUrl);

	}
	
	private String getKibanaDashboardURL(String kibanaUrl) {

		String kibanaDashBoardListUrl = "UNSET_URL";
		try {
			URIBuilder uriBuilder = new URIBuilder(kibanaUrl);
			uriBuilder.setPath("app/kibana");
			uriBuilder.setFragment("dashboards");
			kibanaDashBoardListUrl = uriBuilder.build().toURL().toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return kibanaDashBoardListUrl;
	}
	

	//go to dashboard, records the time when "user" clicked dashboard button
	public long goToDashBoard() {
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
	
	public void clickOnUpdateButton() {
		
		driver.findElement(page.getQuerySubmitButton()).click();
	}
	

	public void setDashBoardTimeFilter(String startTime, String endTime) {
		
		timeFilterDriver.setDashBoardTimeFilter(startTime, endTime);
	}
	
	public void showDateFilter() {
		
		timeFilterDriver.showDateFilter();
	}
	
	public void setFieldFilters(Map<String, String> filters) {
		
		for (Entry<String, String> f: filters.entrySet()) {
			
			fieldFiltersDriver.setFieldFilter(f.getKey(), "is", f.getValue());
			this.waitForDashBoardToFinishLoading();
		}
		
	}

	public void closeChrome() {
		
		driver.close();
	}
	


}
