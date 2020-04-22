package src.main.java.dashboardDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Map;
import java.util.Map.Entry;


import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

import src.main.java.dashboardPage.KibanaNetworkDashBoardPage;

public class NetworkDashBoardPageDriver {

	private WebDriver driver;
	
	
	private TimeFilterDriver timeFilterDriver;
	private FieldFiltersDriver fieldFiltersDriver;
	
	KibanaNetworkDashBoardPage page = new KibanaNetworkDashBoardPage();
	

	public NetworkDashBoardPageDriver (WebDriver driver) {

		this.driver = driver;
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
	public long goToDashBoard(String dashBoardName) {

		WebElement dashboardButton 
			= driver.findElement
			(By.ByLinkText.linkText(dashBoardName));

		dashboardButton.click();
		return System.currentTimeMillis();

	}

	//wait for initial dashboard to finish loading, default is 15 min of data
	public long waitForDashBoardToFinishLoading(Duration timeout) {

		waitForDataRenderingToFinish(timeout);

		return System.currentTimeMillis();
	}
	

	
	public void waitForDataRenderingToFinish(Duration timeout) {
		
		WebDriverWait waitForDataComplete = new WebDriverWait(driver, 100);
		waitForDataComplete.withTimeout(timeout);
		
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
			this.waitForDashBoardToFinishLoading(Duration.ofSeconds(30));
		}
		
	}

	public void closeBrowser() {
		
		driver.close();
	}
	
	//to provide information for error handling
	public String getPageSource() {
		
		return driver.getPageSource();
	}
	
	public void getPageScreenshot(String screenShotPath) {
		
		TakesScreenshot scrShot =((TakesScreenshot)driver);
		File srcFile=scrShot.getScreenshotAs(OutputType.FILE);
		File destFile=new File(screenShotPath);
		try {
			Files.copy(srcFile, destFile);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	


}
