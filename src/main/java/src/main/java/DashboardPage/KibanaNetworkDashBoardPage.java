package src.main.java.DashboardPage;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class KibanaNetworkDashBoardPage {
		
	public TimeSettingBox timeSettingBox = new TimeSettingBox();
	
	private String editQueryDSLButtonName="Edit as Query DSL";
	
	private String addFilterButtonAttr="addFilter";
	
	private String[] widgetNames 
		= {
			"Application", 
			"Application::Role", 
			"Connection History", 
			"LISTEN_Command",
			"LISTEN_LocalPort",
			"LISTEN-METRIC",
			"INCOMING_Command",
			"INCOMING_ForeignAddress",
			"INCOMING_LocalPort",
			"INCOMING-METRIC",
			"OUTGOING_Command",
			"OUTGOING_ForeignAddress",
			"OUTGOING_ForeignPort",
			"OUTGOING-METRIC"
			};
	
	
	public ExpectedCondition<Boolean> buildExpectedConditionsForDataRenderingDone () {
		
		List<ExpectedCondition<WebElement>> elements = new ArrayList<ExpectedCondition<WebElement>> ();
		
		for (String name:widgetNames) {
			elements.add(
					ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//*[@data-title='"+name+"' and @data-render-complete='true']")
					));
		}
		
		return ExpectedConditions.and(
				elements.toArray(new ExpectedCondition[elements.size()]));
		
	}
	
	//field filters
	
	public By getAddFilterButton () {
		
		return By.xpath("//*[@data-test-subj='"+addFilterButtonAttr+"']");
	}
	
	public By getQueryDSLButton () {
		
		return By.xpath("//*[text() = '"+editQueryDSLButtonName+"']");
	}
	
	public By getQueryDSLCodeBox () {
		
		return By.xpath("//*[contains(@class, 'ace_text-input')]");
	}
	
	public By getQueryDSLCodeBoxClickable () {
		
		return By.xpath("//*[contains(@class, 'ace_content')]");
	}
	
	//date settings
	
	public ExpectedCondition<Boolean> buildExpectedConditionsForTimeSettingButtonDone () {
		
		return ExpectedConditions.and(
					ExpectedConditions.visibilityOfElementLocated(this.getStartDateButton()),
					ExpectedConditions.visibilityOfElementLocated(this.getEndDateButton())
				);
	}
	
	public By getShowDatesButton () {
		
		return By.className("euiSuperDatePicker__prettyFormatLink");
		
	}
	
	public By getStartDateButton() {
		
		return By.xpath("//button[@class='euiDatePopoverButton euiDatePopoverButton--start']");
	}
	
	public By getEndDateButton() {
		
		return By.className("//button[@class='euiDatePopoverButton euiDatePopoverButton--end']");
	}

}
