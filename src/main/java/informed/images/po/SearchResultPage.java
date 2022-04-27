package informed.images.po;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import informed.greetings.utils.BasePage;
import informed.greetings.utils.WaitTime;
import informed.greetings.utils.WebElementUtils;

public class SearchResultPage extends BasePage {
	private WebDriver driver;

	@FindBy(xpath = "//input[@name='search']")
	private WebElement searchField;

	@FindBy(xpath = "//span[contains(.,'Sort')]")
	private WebElement recentSearchWithSort;

	@FindBy(xpath = "//span[contains(.,'Keyword')]")
	private WebElement recentSearchWithKeyword;

	@FindBy(xpath = "//div[text()='Refine Search']")
	private WebElement refineSearch;
	
	@FindBy(xpath = "//input[@name='keywords']")
	private WebElement filterByField;
	
	@FindBy(xpath = "//button[text()='Sort & Filter']")
	private WebElement sortAndFilterBtn;
	
	public SearchResultPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public boolean verifyListedImagesAreNotBroken() {
		boolean flag = true;
		int brokenImagesCount = 0;
		try {
			List<WebElement> links = driver.findElements(By.tagName("img"));

			for (int i = 0; i < links.size(); i++) {
				String linkURL = links.get(i).getAttribute("src");
				URL url = new URL(linkURL);
				HttpURLConnection http = (HttpURLConnection) url.openConnection();
				http.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
				http.setConnectTimeout(10000);
				http.setReadTimeout(20000);
				int statusCode = http.getResponseCode();
				if (statusCode == 404 || statusCode == 500) {
					brokenImagesCount = brokenImagesCount + 1;
					System.out.println(linkURL + "and its Status codes is:" + statusCode);
				}
			}
		} catch (Exception e) {

		}
		if (brokenImagesCount > 0)
			flag = false;
		Reporter.log("<br>verifyListedImagesAreNotBroken", true);
		return flag;
	}

	public void clickRefineSearch() {
		refineSearch.click();
		Reporter.log("<br>clicRefineSearch", true);
	}

	public void selectSortByOption(String sortBy) {
		String xpath = "//label[text()='" + sortBy + "']/input";
		driver.findElement(By.xpath(xpath)).click();
		Reporter.log("<br>selectSortByOption:: "+sortBy, true);
	}

	public void filterByKeyword(String keyword) {
		filterByField.sendKeys(keyword);
		sortAndFilterBtn.click();
		Reporter.log("<br>filterByKeyword:: "+keyword, true);
	}

	public void clickRecentSearchesItem(String recentSearchItem) {
		searchField.clear();
		searchField.click();

		String xpath = "//h5[text()='Recent Searches']/following-sibling::div/div/p[text()='" + recentSearchItem + "']";
		new WebDriverWait(driver, WaitTime.SHORT_TO).until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)))
				.click();
		WebElementUtils.SLEEP(2000);
		Reporter.log("<br>clickRecentSearchesItem:: "+recentSearchItem, true);
	}

	public boolean isSortWithTextIsVisibleWithSelection(String selection) {
		boolean flag = false;

		if (recentSearchWithSort.getAttribute("innerText").contains(selection))
			flag = true;
		Reporter.log("<br>isSortWithTextIsVisibleWithSelection:: "+selection, true);
		return flag;
	}

	public boolean isSortWithkeywordIsVisibleWithText(String keyword) {
		boolean flag = false;

		if (recentSearchWithKeyword.getAttribute("innerText").contains(keyword))
			flag = true;
		Reporter.log("<br>isSortWithkeywordIsVisibleWithText:: "+keyword, true);
		return flag;
	}
}
