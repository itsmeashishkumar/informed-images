package informed.images.test;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import informed.images.po.HomePage;
import informed.images.po.LoginPage;
import informed.images.po.PackagesPage;
import informed.images.po.SearchResultPage;
import informed.images.utils.ManageScreenshots;

public class PackagesDownloadTest extends BaseTest{
	private LoginPage loginPage;
	private HomePage homePage;
	private PackagesPage packagesPage;
	private SearchResultPage searchResultPage;
	
	@BeforeClass
	public void beforeClass() {
		driver.get(informedImagesURL);

		loginPage = new LoginPage(driver);
		loginPage.enterUserName("Uspsdemoou");
		loginPage.enterPassword("Opt1m0123!u");
		homePage = loginPage.clickSignIn();
		Assert.assertTrue(homePage.waitUntilStartNowBtnAvailable());
		homePage.clickStartNow();
	}
	
	@Test
	public void testDownloadPackages() {
		driver.get(informedImagesURL);
		packagesPage=homePage.clickPackagesTab();
		
		packagesPage.clickFirstPackage();
		
		packagesPage.agreeToTerms();
		packagesPage.clickDownloadRideAlongImage();
		
		Assert.assertEquals(packagesPage.getDownloadSuccessMsg(),"Thank You! Your Ride-Along Image is downloading.");
	}
	
	@AfterMethod (alwaysRun = true)
	public void takeScreenShot(ITestResult result) {
	    if(ITestResult.FAILURE == result.getStatus()) {
	    	ManageScreenshots.takeScreenShot(result,driver, result.getName());
	    }
	}

}
