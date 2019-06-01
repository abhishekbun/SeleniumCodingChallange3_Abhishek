package org.abk.crm.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.abk.crm.utils.TestUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestBase {
	
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static Properties prop;
	public static Actions action;
	
	public TestBase() {
		
		try {
			prop = new Properties();
			File file = new File(System.getProperty("user.dir")+"/src/main/java/org/abk/crm/config/config.properties");
			FileInputStream fis = new FileInputStream(file);
			prop.load(fis);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}	
	}
	public static void intialization(){
		
		String browser = prop.getProperty("browser");
		System.out.print(browser);
		getPath(browser);
		
		driver.get(prop.getProperty("executionEnvironment"));
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUTS, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICITY_WAIT, TimeUnit.SECONDS);
	
		
	}
	//Provide browser exe path based on OS
	public static void getPath(String browser) 
	{
			String OS=System.getProperty("os.name");
			String driverPath=null;
			if(OS.toUpperCase().contains("WINDOWS"))
			{
				if(browser.toUpperCase().contains("CHROME"))
				{
					driverPath=".//src//main//java//resources//chromedriver.exe";
					System.setProperty("webdriver.chrome.driver", driverPath);
					driver = new ChromeDriver();
				}
				else if(browser.toUpperCase().contains("FF")||browser.toUpperCase().contains("FIRE"))
				{
					driverPath=".//src//main//java//resources//geckodriver.exe";
					System.setProperty("webdriver.gecko.driver", driverPath);
					driver = new FirefoxDriver();
					
				}
				else if(browser.toUpperCase().contains("IE")||browser.toUpperCase().contains("INTERNET EXPLORER"))
				{
					driverPath=".//src//main//java//resources//IEDriverServer.exe";
					System.setProperty("webdriver.ie.driver", driverPath);
					driver = new InternetExplorerDriver();
					
				}
			}
			else if(OS.toUpperCase().contains("MAC"))
			{
				if(browser.toUpperCase().contains("CHROME"))
				{
					driverPath=".//src//main//java//resources//chromedriver";
					System.setProperty("webdriver.chrome.driver", driverPath);
					driver = new ChromeDriver();
				}
				else if(browser.toUpperCase().contains("FF")||browser.toUpperCase().contains("FIRE"))
				{
					driverPath=".//src//main//java//resources//geckodriver";
					System.setProperty("webdriver.gecko.driver", driverPath);
					driver = new FirefoxDriver();
				}
			}
		}
		

}
