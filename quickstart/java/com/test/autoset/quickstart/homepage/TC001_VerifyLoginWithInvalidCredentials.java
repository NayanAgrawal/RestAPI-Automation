package com.test.autoset.quickstart.homepage;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.test.autoset.quickstat.testBase.TestBase;
import com.test.autoset.quickstat.uiAction.HomePage;



public class TC001_VerifyLoginWithInvalidCredentials extends TestBase {

	public static final Logger log = Logger.getLogger(TC001_VerifyLoginWithInvalidCredentials.class.getName());
	HomePage homepage;

	@BeforeTest
	public void setUp() {
		init();
	}

	@Test
	public void verifyLoginWithInvalidCredentials() {

		log.info("----------------Starting verifyLoginWithInvalidCredentials Test-------------------");
		homepage = new HomePage(driver);
		homepage.loginToApplicaation("test@gmail.com", "password123");

		Assert.assertEquals(homepage.getInvalidLoginText(), "Authentication failed.");

		log.info("----------------Finished verifyLoginWithInvalidCredentials Test-------------------");
	}

	@AfterTest
	public void endTest() {
		driver.quit();
	}
}
