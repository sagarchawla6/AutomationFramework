package stepDefinitions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import io.cucumber.java.Before;
//import org.slf4j.LoggerFactory;
//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.Logger;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import pageObjects.AddCustomerPage;
import pageObjects.LoginPage;
import pageObjects.SearchCustomerPage;
import utilities.PageActions;

public class Steps extends BaseClass {

	// Hook- Will be executed once before every TC
	@Before
	public void setup() throws IOException {
		pageActions = new PageActions(driver);
		pageActions.LoadProperties();
		pageActions.SetLoggerConfiguration();
		driver = pageActions.launchBrowser();
	}

	@Given("User Launch Chrome browser")
	public void user_Launch_Chrome_browser() throws IOException {
		lp = new LoginPage(driver);
	}

	@When("User opens URL {string}")
	public void user_opens_URL(String url) {
		lp.launchURL(url);
	}

	@When("User enters Email as {string} and Password as {string}")
	public void user_enters_Email_as_and_Password_as(String email, String password) throws InterruptedException {
		lp.setUserName(email);
		lp.setPassword(password);
	}

	@When("Click on Login")
	public void click_on_Login() throws InterruptedException {
		lp.clickLogin();
	}

	@Then("Page Title should be {string}")
	public void page_Title_should_be(String pagetitle) {
		lp.verifyPageTitle(pagetitle);
	}

	@When("User click on Log out link")
	public void user_click_on_Log_out_link() throws InterruptedException {
		lp.clickLogout();
		Thread.sleep(3000);
	}

	@Then("close browser")
	public void close_browser() {
		driver.quit();
	}

	// Customer feature step definitions

	@Then("User can view Dashboad")
	public void user_can_view_Dashboad() throws InterruptedException {
		addCust = new AddCustomerPage(driver);
		addCust.verifylogin();
		Assert.assertEquals("Dashboard / nopCommerce administration", addCust.getPageTitle());
	}

	@When("User click on customers Menu")
	public void user_click_on_customers_Menu() throws InterruptedException {
		addCust.clickOnCustomersMenu();
	}

	@When("click on customers Menu Item")
	public void click_on_customers_Menu_Item() throws InterruptedException {
		addCust.clickOnCustomersMenuItem();
	}

	@When("click on Add new button")
	public void click_on_Add_new_button() throws InterruptedException {
		Thread.sleep(2000);
		addCust.clickOnAddnew();
		Thread.sleep(2000);
	}

	@Then("User can view Add new customer page")
	public void user_can_view_Add_new_customer_page() {
		Assert.assertEquals("Add a new customer / nopCommerce administration", addCust.getPageTitle());

	}

	@When("User enter customer info")
	public void user_enter_customer_info() throws InterruptedException {
		String email = ramdomString() + "@gmail.com";
		addCust.setEmail(email);
		addCust.setPassword("test123");
		// Registered - default
		// The customer cannot be in both 'Guests' and 'Registered' customer roles
		// Add the customer to 'Guests' or 'Registered' customer role
		addCust.setCustomerRoles("Guest");
		Thread.sleep(3000);

		addCust.setManagerOfVendor("Vendor 2");
		addCust.setGender("Male");
		addCust.setFirstName("Pavan");
		addCust.setLastName("Kumar");
		addCust.setDob("7/05/1985"); // Format: D/MM/YYY
		addCust.setCompanyName("busyQA");
		addCust.setAdminContent("This is for testing.........");

	}

	@When("click on Save button")
	public void click_on_Save_button() throws InterruptedException {
		addCust.clickOnSave();
		Thread.sleep(2000);
	}

	@Then("User can view confirmation message {string}")
	public void user_can_view_confirmation_message(String successmessage) {
		Assert.assertTrue(driver.findElement(By.tagName("body")).getText().contains(successmessage));
	}

	// Steps for searching a customer using email id

	@When("Enter customer EMail")
	public void enter_customer_EMail() {
		searchCust = new SearchCustomerPage(driver);
		searchCust.setEmail("victoria_victoria@nopCommerce.com");
	}

	@When("Click on search button")
	public void click_on_search_button() throws InterruptedException {
		searchCust.clickSearch();
	}

	@Then("User should found Email in the Search table")
	public void user_should_found_Email_in_the_Search_table() {
		boolean status = searchCust.searchCustomerByEmail("victoria_victoria@nopCommerce.com");
		Assert.assertTrue(status);
	}

	// Steps for searching a customer using name

	@When("Enter customer FirstName")
	public void enter_customer_FirstName() {
		searchCust = new SearchCustomerPage(driver);
		searchCust.setFirstName("Victoria");

	}

	@When("Enter customer LastName")
	public void enter_customer_LastName() {
		searchCust.setLastName("Terces");
	}

	@Then("User should found Name in the Search table")
	public void user_should_found_Name_in_the_Search_table() {
		Assert.assertTrue(searchCust.searchCustomerByName("Victoria Terces"));
	}

}
