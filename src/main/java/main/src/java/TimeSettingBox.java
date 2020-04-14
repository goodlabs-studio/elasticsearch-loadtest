package main.src.java;
import org.openqa.selenium.By;

public class TimeSettingBox {

	
	private final String ABSOLUTE_BUTTON_ID = "absolute";
	private final String ATTR_DATA_TEST_SUBJ_VALUE = "superDatePickerAbsoluteDateInput";
	
	public By getAbsoluteButton () {
		
		return By.id(ABSOLUTE_BUTTON_ID);
	}
	
	public By byGetStartDateBox() {
		
		return By.xpath("//*[@data-test-subj='"+ATTR_DATA_TEST_SUBJ_VALUE+"']");
	}
	
}
