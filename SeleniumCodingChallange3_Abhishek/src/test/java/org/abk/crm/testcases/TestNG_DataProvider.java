package org.abk.crm.testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestNG_DataProvider {
	
	WebDriver driver;
	@Test
	@Parameters({"url","userName","password"})
	public void login(String url, String userName, String password) {
		String driverPath=".//src//main//java//resources//chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", driverPath);
		
		driver = new ChromeDriver();
		driver.get(url);
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//input[@id='Login1_UserName']")).sendKeys(userName);
		driver.findElement(By.xpath("//input[@id='Login1_Password']")).sendKeys(password);
		driver.findElement(By.xpath("//input[@id='Login1_LoginButton']")).click();
		
	}

}
