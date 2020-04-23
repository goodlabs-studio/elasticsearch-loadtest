package src.main.java.searchGuardDriver;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import src.main.java.dashboardDriver.NetworkDashBoardPageDriver;
import src.main.java.kibanaMainPage.KibanaMainPage;
import src.main.java.searchGuardPage.SearchGuardLoginPage;

public class SearchGuardLoginDriver {
	
	final static Logger logger 
		= LogManager.getLogger(SearchGuardLoginDriver.class);
	
	WebDriver driver;
	WebDriverWait waitDriver;
	SearchGuardLoginPage page;
	KibanaMainPage kibanaPage;
	
	public SearchGuardLoginDriver(WebDriver driver) {
		
		this.driver = driver;
		page = new SearchGuardLoginPage();
		kibanaPage = new KibanaMainPage();
		
	}
	
	public void loadLoginPage(String url) {
		
		URIBuilder uriBuilder;

		try {
			uriBuilder = new URIBuilder(url);
			uriBuilder.setPath("login");
			String loginUrl = uriBuilder.build().toURL().toString();
			driver.get(loginUrl);
		} catch (URISyntaxException | MalformedURLException e) {
			
			logger.error(e.getMessage());
		}
		
		
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
	
	public void waitForLogin() {
		
		driver.findElement(kibanaPage.getDashBoardIcon());
	}


}
