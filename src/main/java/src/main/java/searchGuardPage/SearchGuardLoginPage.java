package src.main.java.searchGuardPage;

import org.openqa.selenium.By;

public class SearchGuardLoginPage {

	public By getUserNameInput () {
		
		return By.xpath("//*[@ng-model='ui.credentials.username']");
	}
	
	public By getPasswordInput() {
		
		return By.xpath("//*[@ng-model='ui.credentials.password']");
	}
	
	public By getLoginButton() {

		return By.xpath("//button[@id='sg.login']");
	}
}
