package src.main.java.dashboardPage;

import org.openqa.selenium.By;

public class FilterSettingBox {

	public By getFilterFieldComboBox () {
		
		return By.xpath("//*[@data-test-subj='filterFieldSuggestionList']");
	}
	
	public By getFilterOperatorComboBox () {
		
		return By.xpath("//*[@data-test-subj='filterOperatorList']");
	}
	
	
	/*i.e
	 * //span[contains(text(),'applications') and @class='euiComboBoxOption__content']
	 */
	public By getSelectOption(String value) {
	
		return By.xpath
				("//span[contains(text(),'"+value+"') and @class='euiComboBoxOption__content']");
	}
	

	public By getValueInputBox() {
		
		return By.xpath
				("//div[@data-test-subj='filterParams']//*[@data-test-subj='comboBoxSearchInput']");
	}
	
	public By getSaveFilterButton() {
		
		return By.xpath
				("//button[@data-test-subj='saveFilter']");
	}
	
}
