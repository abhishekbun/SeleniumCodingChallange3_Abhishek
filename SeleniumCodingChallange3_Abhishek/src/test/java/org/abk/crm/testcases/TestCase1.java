package org.abk.crm.testcases;

import org.abk.crm.base.TestBase;
import org.abk.crm.pages.HomePage;
import org.abk.crm.pages.LoginPage;
import org.abk.crm.utils.TestUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestCase1 extends TestBase {
	
	LoginPage loginPage;
	HomePage homePage;
	
	public TestCase1() {
		super();
	}
	@BeforeMethod
	public void setUp() {
		intialization();
		loginPage = new LoginPage();
	}
	@Test
	public void testCase1() throws InterruptedException{
		
		homePage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		TestUtil.pageLoad_ImplicitWait();
		String titleOFPage = driver.getTitle();
		
		for(int i=1;i<=10;i++) {
			titleOFPage = driver.getTitle();
			System.out.println("TITLE OF THE PAGE ::: "+titleOFPage);
			if(titleOFPage.equalsIgnoreCase("Expired page")) {
				driver.navigate().back();
				TestUtil.pageLoad_ImplicitWait();
				homePage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
			}
			else {
				break;
			}
		}	
	}
}
