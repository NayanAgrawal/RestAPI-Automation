package com.test.autoset.quickstart.homepage;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.test.autoset.quickstat.testBase.TestBase;
import com.test.autoset.quickstat.uiAction.HomePage;

public class TC002_verifyRegistration extends TestBase{


	public static final Logger log = Logger.getLogger(TC002_verifyRegistration.class.getName());
	HomePage homepage;

	@BeforeTest
	public void setUp() {
		init();
	}

	@Test
	public void verifyRegistrationSuccess() {
		log.info("----------------Starting verifyLoginWithInvalidCredentials Test-------------------");
		homepage = new HomePage(driver);
		homepage.RegisterToApplicaation("Test", "lastName", "address", "emailAddress@gmail.com", "155151", "Password@123",
				"Password@123");
		Assert.assertEquals(true, homepage.VerifyRegistrationSuccess());
		log.info("----------------Finished verifyRegistrationSuccess Test-------------------");
	}

	@AfterTest
	public void endTest() {
		driver.quit();
	}


}
