package org.abk.crm.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class People_OrganizationPage {

	@FindBy(xpath="//a[@href='/parties']")
	WebElement peopleOrganizationButton;
	
	@FindBy(xpath="//a[text()='Add Person']")
	WebElement addPersonButton;
	
	
	
}
