package com.test.autoset.quickstart.uiActions;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.test.autoset.quickstart.homepage.TC001_VerifyLoginWithInvalidCredentials;

public class HomePage {
	

	public static final Logger log = Logger.getLogger(TC001_VerifyLoginWithInvalidCredentials.class.getName());

	WebDriver driver;
	
	@FindBy(xpath=".//*[@id='header']/div[2]/div/div/nav/div[1]/a")
	WebElement signIn;
	
	@FindBy(xpath=".//*[@id='email']")
	WebElement loginEmailAddress;
	
	@FindBy(xpath=".//*[@id='passwd']")
	WebElement loginPassword;
	
	@FindBy(id="SubmitLogin")
	WebElement submit;
	
	@FindBy(xpath=".//*[@id='center_column']/div[1]/ol/li")
	WebElement athanticationFailed;
	
	public HomePage(WebDriver driver){
		PageFactory.initElements(driver, this);
	}
	
	public void loginToApplicaation(String emailAddress, String password){
		signIn.click();
		log.info("Clicked on sign in and object is : "+ signIn.toString());
		loginEmailAddress.sendKeys(emailAddress);
		log.info("Enter email address "+ emailAddress +" and object is : "+ loginEmailAddress.toString());
		loginPassword.sendKeys(password);
		log.info("Enter password" + password + " and object is : "+ loginPassword.toString());
		submit.click();
		log.info("Clicked on submit button and the object is : " + submit.toString());
	}
	
	public String getInvalidLoginText(){
		return athanticationFailed.getText();
	}
}
