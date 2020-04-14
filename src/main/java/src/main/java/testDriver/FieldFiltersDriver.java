package src.main.java.testDriver;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import src.main.java.ESQueryBuilder;
import src.main.java.DashboardPage.KibanaNetworkDashBoardPage;

public class FieldFiltersDriver {

	private WebDriver driver;
	private KibanaNetworkDashBoardPage page;
	private ESQueryBuilder esQueryBuilder;
	
	public FieldFiltersDriver (WebDriver driver, KibanaNetworkDashBoardPage page) {
		
		this.esQueryBuilder = new ESQueryBuilder();
		this.page = page;
		this.driver = driver;
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

		pasteIntoTextBox (esQueryBuilder.buildQuery("march","april"), codeBox, driver);
		//codeBox.sendKeys(esQueryBuilder.buildQuery(startTime, endTime));
	}
	
	private void pasteIntoTextBox (String text, WebElement textBox, WebDriver driver) {
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection selection = new StringSelection(text);
		clipboard.setContents(selection, selection);
		textBox.sendKeys(Keys.chord(Keys.LEFT_CONTROL, "v"));

	}
	
}
