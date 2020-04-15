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

import src.main.java.ESQueryBuilder;
import src.main.java.DashboardPage.KibanaNetworkDashBoardPage;

public class FieldFiltersDriver {

	private WebDriver driver;
	private KibanaNetworkDashBoardPage page;
	private ESQueryBuilder esQueryBuilder;
	private WebDriverWait wait;
	
	public FieldFiltersDriver (WebDriver driver, KibanaNetworkDashBoardPage page) {
		
		this.esQueryBuilder = new ESQueryBuilder();
		this.page = page;
		this.driver = driver;
		this.wait = new WebDriverWait(driver,500);
	}
	
	public void setFieldFiltersByQuery (Map<String, String> filters) {
		
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
		
		pasteIntoTextBox (esQueryBuilder.buildQueryFromList(filters), codeBox, driver);

	}
	
	private void pasteIntoTextBox (String text, WebElement textBox, WebDriver driver) {
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection selection = new StringSelection(text);
		clipboard.setContents(selection, selection);
		textBox.sendKeys(Keys.chord(Keys.LEFT_CONTROL, "v"));

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
