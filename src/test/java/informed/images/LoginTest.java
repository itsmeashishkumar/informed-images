package informed.images;

import org.testng.Assert;
import org.testng.annotations.Test;

import informed.images.BaseTest;
import informed.images.po.InformedImagesHomePage;
import informed.images.po.InformedImagesLoginPage;

public class LoginTest extends BaseTest {
	private InformedImagesLoginPage loginPage;
	private InformedImagesHomePage homePage;

	@Test
	public void loginWithValidCredentials() {
		driver.get(informedImagesURL);

		loginPage = new InformedImagesLoginPage(driver);
		loginPage.enterUserName("Uspsdemoou");
		loginPage.enterPassword("Opt1m0123!u");
		homePage = loginPage.clickSignIn();
		Assert.assertTrue(homePage.waitUntilStartNowBtnAvailable());
		homePage.clickStartNow();
	}
}
