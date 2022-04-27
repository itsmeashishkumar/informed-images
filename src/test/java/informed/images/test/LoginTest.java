package informed.images.test;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import informed.images.po.HomePage;
import informed.images.po.LoginPage;
import informed.images.utils.ManageScreenshots;

public class LoginTest extends BaseTest {
	private LoginPage loginPage;
	private HomePage homePage;

	@Test
	public void loginWithValidCredentials() {
		driver.get(informedImagesURL);

		loginPage = new LoginPage(driver);
		loginPage.enterUserName("Uspsdemoou");
		loginPage.enterPassword("Opt1m0123!u");
		homePage = loginPage.clickSignIn();
		Assert.assertTrue(homePage.waitUntilStartNowBtnAvailable());
		homePage.clickStartNow();
	}
	
	@AfterMethod (alwaysRun = true)
	public void takeScreenShot(ITestResult result) {
	    if(ITestResult.FAILURE == result.getStatus()) {
	    	ManageScreenshots.takeScreenShot(result,driver, result.getName());
	    }
	}
}
