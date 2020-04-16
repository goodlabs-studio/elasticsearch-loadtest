package src.main.java.common;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CommonUtil {

	private static CommonUtil utils = new CommonUtil();
	
	public static CommonUtil  getCommonUtil( ) {
		
		return utils;
	}
	
	private CommonUtil() {
		
	}
	
	public void clickOnStaleElement(By elementLoc, int numAttempts, WebDriver driver) {
		
		int attempt = 0;
		while (attempt < numAttempts) {
			try {
				WebElement button = driver.findElement(elementLoc);
				button.click();
			break;
			} catch (StaleElementReferenceException e) {
			
				System.out.println("retrying stale element..");
				attempt++;
			}
		}
	}
}
