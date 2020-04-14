package src.main.java.testDriver;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import src.main.java.DashboardPage.KibanaNetworkDashBoardPage;

public class TimeFilterDriver {

	private WebDriver driver;
	private KibanaNetworkDashBoardPage page;
	
	public TimeFilterDriver (WebDriver driver, KibanaNetworkDashBoardPage page) {
		
		this.page = page;
		this.driver = driver;
	}
	
	
	//set time filters
	public void setDashBoardTimeFilter(String startTime, String endTime) {
		

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
	
}
