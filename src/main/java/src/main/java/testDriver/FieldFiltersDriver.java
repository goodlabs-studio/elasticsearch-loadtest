package src.main.java.testDriver;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import src.main.java.DashboardPage.KibanaNetworkDashBoardPage;
import src.main.java.common.CommonUtil;

public class FieldFiltersDriver {

	private WebDriver driver;
	private KibanaNetworkDashBoardPage page;

	private WebDriverWait wait;
	
	public FieldFiltersDriver (WebDriver driver, KibanaNetworkDashBoardPage page) {
		

		this.page = page;
		this.driver = driver;
		
		this.wait = new WebDriverWait(driver,10);
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

		wait.until(ExpectedConditions.presenceOfElementLocated(selectDropDownOptionLoc));
		WebElement selectionOption = driver.findElement(selectDropDownOptionLoc);

		wait.until(
				ExpectedConditions.and(
				
				ExpectedConditions.visibilityOf(selectionOption),
				ExpectedConditions.elementToBeClickable(selectionOption)
						));
		
		Actions actions = new Actions(driver);
		
		//scroll to element in dropdown
		actions.moveToElement(selectionOption).build().perform();
		selectionOption.click();
	}
	

	
	

	
}
