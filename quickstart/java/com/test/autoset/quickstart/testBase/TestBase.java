package com.test.autoset.quickstart.testBase;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestBase {
	
	public static final Logger log = Logger.getLogger(TestBase.class.getName());

	public WebDriver driver;
	String url = "http://automationpractice.com/index.php";
	String browser = "Chrome";

	public void init() {

		selectBrowser(browser);
		getUrl(url);
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		

	}

	public void selectBrowser(String browser) {
		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/Drivers/chromedriver.exe");
			log.info("Creating object of " + browser);
			
			driver = new ChromeDriver();

		}

	}

	public void getUrl(String url) {
		log.info("Navigating to : " + url);
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}
}
