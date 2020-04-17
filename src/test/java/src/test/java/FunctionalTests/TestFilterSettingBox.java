package src.test.java.FunctionalTests;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import src.main.java.DashboardPage.FilterSettingBox;

public class TestFilterSettingBox {

	@Test
	public void test() {
		FilterSettingBox box = new FilterSettingBox();
		assertEquals (
				By.xpath("//span[contains(text(),'applications') and @class='euiComboBoxOption__content']"), 
				box.getSelectOption("applications")
				);
	}

}
