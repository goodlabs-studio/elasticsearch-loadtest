package src.main.java.searchGuardDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import src.main.java.searchGuardPage.SearchGuardLoginPage;

public class SearchGuardLoginDriver {
	
	WebDriver driver;
	WebDriverWait waitDriver;
	SearchGuardLoginPage page;
	
	public SearchGuardLoginDriver(WebDriver driver) {
		
		this.driver = driver;
		page = new SearchGuardLoginPage();
		
	}
	
	public void inputUserName(String userName) {
		
		WebElement usernameInput = driver.findElement(
					page.getUserNameInput());
		usernameInput.sendKeys(userName);

	}
	
	public void inputPassword(String password) {
		
		WebElement usernameInput = driver.findElement(
				page.getPasswordInput());
		usernameInput.sendKeys(password);
	}
	
	public void clickLoginButton() {
		
		WebElement loginButton = driver.findElement(page.getLoginButton());
		loginButton.click();
	}

}
