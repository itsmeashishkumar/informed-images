package informed.images;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import informed.images.po.InformedImagesHomePage;
import informed.images.po.InformedImagesLoginPage;
import informed.images.po.PackagesPage;
import informed.images.po.SearchResultPage;

public class PackagesSearchTest extends BaseTest {
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
	public void verifySearchWithValidKeyword() {
		driver.get(informedImagesURL);
		packagesPage=homePage.clickPackagesTab();
		
		searchResultPage = packagesPage.searchWithKeyword("sale");

		Assert.assertTrue(searchResultPage.verifyListedImagesAreNotBroken());
	}

	@Test
	public void verifySearchWithRecentSearchOptions() {
		driver.get(informedImagesURL);
		packagesPage=homePage.clickPackagesTab();
		
		homePage.searchWithKeyword("sale");
		homePage.searchWithKeyword("free");
		searchResultPage = homePage.searchWithKeyword("shop");

		driver.navigate().refresh();
		searchResultPage.clickRecentSearchesItem("sale");
		Assert.assertTrue(searchResultPage.verifyListedImagesAreNotBroken());

		searchResultPage.clickRecentSearchesItem("free");
		Assert.assertTrue(searchResultPage.verifyListedImagesAreNotBroken());

		searchResultPage.clickRecentSearchesItem("shop");
		Assert.assertTrue(searchResultPage.verifyListedImagesAreNotBroken());

	}

	@Test
	public void verifySearchWithRefineSearch() {
		driver.get(informedImagesURL);
		packagesPage=homePage.clickPackagesTab();
		
		searchResultPage = homePage.searchWithKeyword("sale");
		searchResultPage.clickRefineSearch();

		searchResultPage.selectSortByOption("Newest");
		searchResultPage.filterByKeyword("sale");

		Assert.assertTrue(searchResultPage.isSortWithTextIsVisibleWithSelection("newest"));

		Assert.assertTrue(searchResultPage.isSortWithkeywordIsVisibleWithText("sale"));

	}


}
