package com.test.autoset.quickstart.homepage;

import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.test.autoset.quickstat.testBase.TestBase;
import com.test.autoset.quickstat.uiAction.HomePage;

public class TC003_VerifyLoginWithDifferentRecords extends TestBase {

	HomePage homePage;	

	@BeforeClass
	public void setup() {
		init();
	}

	
	@DataProvider(name = "loginData")
	public String[][] getTestData() throws Exception{
		String[][] testRecords = getData("DataRead.xlsx", "NT");
		System.out.println("Records -----------" + testRecords);
		
		return testRecords;
	}
		
	
	@Test(dataProvider = "loginData")
	public void testLogin(String emailAddress,String  password,String runMode) {
		
		if(runMode.equalsIgnoreCase("n")){
			throw new  SkipException("User makred it as no run");
		}
		homePage = new HomePage(driver);
		homePage.loginToApplicaation(emailAddress, password);
		getScreenshot("testLogin"+emailAddress+"_");
	}

	@AfterClass
	public void endTest() {
		driver.quit();
	}

}
