package informed.images.test;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import informed.images.po.HomePage;
import informed.images.po.LoginPage;
import informed.images.po.SearchResultPage;
import informed.images.utils.ManageScreenshots;

public class MailSearchTest extends BaseTest {
	private LoginPage loginPage;
	private HomePage homePage;
	private SearchResultPage searchResultPage;

	@BeforeClass
	public void beforeTest() {
		driver.get(informedImagesURL);

		loginPage = new LoginPage(driver);
		loginPage.enterUserName("Uspsdemoou");
		loginPage.enterPassword("Opt1m0123!u");
		homePage = loginPage.clickSignIn();
		Assert.assertTrue(homePage.waitUntilStartNowBtnAvailable());
		homePage.clickStartNow();
	}
	
	@Test
	public void verifySearchWithValidKeyword() {
		driver.get(informedImagesURL);
		searchResultPage = homePage.searchWithKeyword("sale");

		Assert.assertTrue(searchResultPage.verifyListedImagesAreNotBroken());
	}

	@Test
	public void verifySearchWithRecentSearchOptions() {
		driver.get(informedImagesURL);
		homePage.searchWithKeyword("sale");
		homePage.searchWithKeyword("offer");
		searchResultPage = homePage.searchWithKeyword("shop");

		driver.navigate().refresh();
		searchResultPage.clickRecentSearchesItem("sale");
		Assert.assertTrue(searchResultPage.verifyListedImagesAreNotBroken());

		searchResultPage.clickRecentSearchesItem("offer");
		Assert.assertTrue(searchResultPage.verifyListedImagesAreNotBroken());

		searchResultPage.clickRecentSearchesItem("shop");
		Assert.assertTrue(searchResultPage.verifyListedImagesAreNotBroken());

	}

	@Test
	public void verifySearchWithRefineSearch() {
		driver.get(informedImagesURL);
		searchResultPage = homePage.searchWithKeyword("offer");
		searchResultPage.clickRefineSearch();

		searchResultPage.selectSortByOption("Newest");
		searchResultPage.filterByKeyword("offer");

		Assert.assertTrue(searchResultPage.isSortWithTextIsVisibleWithSelection("newest"));

		Assert.assertTrue(searchResultPage.isSortWithkeywordIsVisibleWithText("offer"));

	}
	
	@AfterMethod (alwaysRun = true)
	public void takeScreenShot(ITestResult result) {
	    if(ITestResult.FAILURE == result.getStatus()) {
	    	ManageScreenshots.takeScreenShot(result,driver, result.getName());
	    }
	}
}
