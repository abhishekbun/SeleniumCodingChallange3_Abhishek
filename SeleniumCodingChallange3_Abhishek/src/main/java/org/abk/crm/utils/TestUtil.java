package org.abk.crm.utils;

import java.util.concurrent.TimeUnit;

import org.abk.crm.base.TestBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestUtil extends TestBase{
	
	public static final long PAGE_LOAD_TIMEOUTS = 120;
	public static final long IMPLICITY_WAIT = 180;
	public static final long EXPLICITY_WAIT = 180;
	
	public static void waitForElementClickable(WebElement element) {
		
		wait = new WebDriverWait(driver, EXPLICITY_WAIT);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		
	}
	
	//Wait for both page and implicit wait
	public static void pageLoad_ImplicitWait() {
		
		driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUTS, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(IMPLICITY_WAIT, TimeUnit.SECONDS);
		
	}
	
}
