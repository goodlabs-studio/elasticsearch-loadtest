package src.main.java.testDriver;


import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;


import src.main.java.DashboardPage.KibanaNetworkDashBoardPage;


public class FieldFiltersDriver {

	private WebDriver driver;
	private KibanaNetworkDashBoardPage page;

	
	public FieldFiltersDriver (WebDriver driver, KibanaNetworkDashBoardPage page) {
		

		this.page = page;
		this.driver = driver;

	}
	
	
	public void setFieldFilter(String field, String operator, String value) {
		
		WebElement addFilterButton = 
				driver.findElement(page.getAddFilterButton());
		addFilterButton.click();

		
		setDropDown(page.filterSettingBox.getFilterFieldComboBox(), 
					field);
		
		
		setDropDown(page.filterSettingBox.getFilterOperatorComboBox(), 
				operator);
		
		WebElement valueInputBox = 
				driver.findElement(page.filterSettingBox.getValueInputBox());
		
		valueInputBox.click();
		valueInputBox.sendKeys(value);
		
		WebElement saveFilterButton =
				driver.findElement(page.filterSettingBox.getSaveFilterButton());
		saveFilterButton.click();
	}
	
	private void setDropDown(By dropdown, String value) {

		//annoyingly, the dropdown isn't of "select" class


		driver.findElement(dropdown).click();
		
		By selectDropDownOptionLoc = page.filterSettingBox.getSelectOption(value);
		WebElement selectionOption = driver.findElement(selectDropDownOptionLoc);

		//scroll to element in dropdown
		Actions actions = new Actions(driver);
		actions.moveToElement(selectionOption).build().perform();
		
		selectionOption.click();
	}
	

	
	

	
}
