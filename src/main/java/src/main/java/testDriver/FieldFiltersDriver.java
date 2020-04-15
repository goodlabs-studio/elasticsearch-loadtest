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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import src.main.java.DashboardPage.KibanaNetworkDashBoardPage;

public class FieldFiltersDriver {

	private WebDriver driver;
	private KibanaNetworkDashBoardPage page;

	private WebDriverWait wait;
	
	public FieldFiltersDriver (WebDriver driver, KibanaNetworkDashBoardPage page) {
		

		this.page = page;
		this.driver = driver;
		this.wait = new WebDriverWait(driver,500);
	}
	
	public void setFieldFilters(Map<String, String> filters) {
		
		setFieldFilter("applications", "is", "Application-12");
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
	}
	
	private void setDropDown(By dropdown, String value) {

		//annoyingly, the dropdown isn't of "select" class

		driver.findElement(dropdown).click();
		wait.until(ExpectedConditions.
				elementToBeClickable(page.filterSettingBox.
						getSelectOption(value))).click();

	}
	

	
	

	
}
