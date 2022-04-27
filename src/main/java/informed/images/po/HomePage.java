package informed.images.po;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import informed.images.utils.BasePage;
import informed.images.utils.WaitTime;
import informed.images.utils.WebElementUtils;

public class HomePage extends BasePage {
	private WebDriver driver;

	@FindBy(xpath = "//button[text()='Start Now']")
	private WebElement startNow;

	@FindBy(xpath = "//input[@name='search']")
	private WebElement searchField;

	@FindBy(xpath = "//input[@name='search']/../../*[local-name()='svg']")
	private WebElement searchIcon;

	@FindBy(xpath = "//button[text()='Get Started']")
	private WebElement getStarted;

	@FindBy(xpath = "//div[text()='Postcard']")
	private WebElement postCard;

	@FindBy(xpath = "//div[text()='Letter-Sized ']")
	private WebElement letterSizedEnvelope;

	@FindBy(xpath = "//div[text()='Card-Size ']")
	private WebElement cardSizeEnvelope;
	
	@FindBy(xpath = "//h5[text()='Packages']")
	private WebElement packagesTab;
	
	public HomePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public HomePage clickStartNow() {
		startNow.click();
		Reporter.log("<br>clickStartNow", true);
		return new HomePage(driver);
	}

	public SearchResultPage searchWithKeyword(String searchKeyword) {
		searchField.clear();
		searchField.sendKeys(searchKeyword);
		WebElementUtils.SLEEP(3000);
		searchIcon.click();

		new WebDriverWait(driver, WaitTime.SHORT_TO).until(webDriver -> ((JavascriptExecutor) driver)
				.executeScript("return document.readyState").equals("complete"));
		Reporter.log("<br>searchWithKeyword:: " + searchKeyword, true);
		return new SearchResultPage(driver);
	}

	public boolean waitUntilStartNowBtnAvailable() {
		boolean flag = false;
		try {
			new WebDriverWait(driver, WaitTime.SHORT_TO).until(ExpectedConditions.visibilityOf(startNow));
			flag = true;
		} catch (Exception e) {
			flag = false;
		}
		Reporter.log("<br>waitUntilStartNowBtnAvailable", true);
		return flag;
	}

	public PackagesPage clickPackagesTab() {
		packagesTab.click();
		return new PackagesPage(driver);
	}
}
