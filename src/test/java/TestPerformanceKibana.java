import static org.junit.Assert.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestPerformanceKibana {

	WebDriver driver;
	private final String CHROME_LOCATION = "C:\\Users\\Richard\\Documents\\Development\\Selenium\\KibanaDataTest\\src\\main\\resources\\chromedriver_win32\\chromedriver.exe";
	private final String BASE_URL = "http://localhost:5601/app/kibana#/dashboards";
	
	private final int shareDataItemsCount=14;
	private String[] widgetNames = {"LISTEN_Command"};
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

	//wait for initial dashboard to finish loading
	public long waitForDashBoardToFinishLoading() {

		
		
		
		return System.currentTimeMillis();
	}
	
	
	private void setDashBoardSettings() {
		
		
		
	}
	
	
	public void waitForDataRenderingToFinish() {
		
		WebDriverWait waitForDataComplete = new WebDriverWait(driver, 100);
		
		waitForDataComplete.
			until( page.buildExpectedConditionsForDataRenderingDone());
	}
	
	
	@Test
	public void test() {
		loadKibanaPage(BASE_URL);
		
		
		long startTime = goToDashBoard();
		//waitForDashBoardToFinish();
		waitForDataRenderingToFinish();
		long duration = System.currentTimeMillis() - startTime;
		System.out.println(duration);
	}

}
