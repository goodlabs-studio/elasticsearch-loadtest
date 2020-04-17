package src.main.java.testDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import src.main.java.DashboardPage.KibanaNetworkDashBoardPage;
import src.main.java.common.CommonUtil;

public class TimeFilterDriver {

	private WebDriver driver;
	private KibanaNetworkDashBoardPage page;
	private WebDriverWait wait;
	
	public TimeFilterDriver (WebDriver driver, KibanaNetworkDashBoardPage page) {
		
		this.page = page;
		this.driver = driver;
		this.wait = new WebDriverWait(driver,10);
	}
	
	public void showDateFilter () {
		
		WebElement showDatesButton = 
				driver.findElement(page.getShowDatesButton());
		showDatesButton.click();
	}
	
	//set time filters
	public void setDashBoardTimeFilter(String startTime, String endTime) {


		//find and set startDateBox value
		WebElement startDateBox = 
				goToAndGetDateBox(page.getStartDateButton(), 
						page.timeSettingBox.byGetDateInputBox());
		
		setDateInDateBox(startDateBox, startTime, wait);
		
		
		
		//find and set endDateBox value
		WebElement endDateBox = 
				goToAndGetDateBox(page.getEndDateButton(), 
						page.timeSettingBox.byGetDateInputBox());
		
		setDateInDateBox(endDateBox, endTime, wait);

	}
	
	
	private WebElement goToAndGetDateBox(By showDateCalendarButton, By dateInputBox) {

		WebElement dateCalendarButton = 
				driver.findElement(showDateCalendarButton);
		dateCalendarButton.click();
		//Kibana does something weird with the absolute button 
		//where it goes stale the second time you load the dialogue box, this is to get around it
		CommonUtil.getCommonUtil().
			clickOnStaleElement(page.timeSettingBox.getAbsoluteButton(), 100, driver);
		

		return driver.findElement(dateInputBox);

	}
	

	
	
	private void setDateInDateBox (WebElement dateBox, String date, WebDriverWait wait) {
		
		wait.until(ExpectedConditions.elementToBeClickable(dateBox)).click();
		
		//getting around annoying textbox becoming uninteractable upon using .clear() method
		dateBox.sendKeys(Keys.CONTROL + "a");
		dateBox.sendKeys(Keys.DELETE);
		dateBox.sendKeys(date);
	}
	
}
