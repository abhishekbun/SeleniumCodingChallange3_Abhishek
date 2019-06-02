package org.abk.crm.pages;

import org.abk.crm.base.TestBase;
import org.abk.crm.utils.TestUtil;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends TestBase{
	
	@FindBy(xpath="//input[@id='login:usernameDecorate:username']")
	WebElement username;
	
	@FindBy(xpath="//input[@id='login:passwordDecorate:password']")
	WebElement password;
	
	//@FindBy(xpath="//*[@id=\'login:login\']")
	@FindBy(id="login:login")
	WebElement loginButton;
	
	public LoginPage() {
		PageFactory.initElements(driver, this);	
	}
	
	public HomePage login(String uname, String pwd) throws InterruptedException {

		username.sendKeys(uname);
		password.sendKeys(pwd);
		Thread.sleep(500);
		loginButton.click();
		TestUtil.pageLoad_ImplicitWait();
		return new HomePage();
	}
	

}
