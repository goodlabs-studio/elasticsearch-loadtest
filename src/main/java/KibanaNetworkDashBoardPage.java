import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class KibanaNetworkDashBoardPage {
	
	String[] widgetNames 
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
	
}
