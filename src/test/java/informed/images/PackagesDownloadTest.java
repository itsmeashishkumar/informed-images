package informed.images;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import informed.images.po.InformedImagesHomePage;
import informed.images.po.InformedImagesLoginPage;
import informed.images.po.PackagesPage;
import informed.images.po.SearchResultPage;

public class PackagesDownloadTest extends BaseTest{
	private InformedImagesLoginPage loginPage;
	private InformedImagesHomePage homePage;
	private PackagesPage packagesPage;
	private SearchResultPage searchResultPage;
	
	@BeforeClass
	public void beforeClass() {
		driver.get(informedImagesURL);

		loginPage = new InformedImagesLoginPage(driver);
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

}
