package informed.images.po;

import org.openqa.selenium.By;
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

public class PackagesPage extends BasePage{
	
	public PackagesPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	private WebDriver driver;
	
	@FindBy(xpath = "//button[@type='submit']")
	private WebElement nextBtn;

	@FindBy(xpath = "//input[@name='search']")
	private WebElement searchField;
	
	@FindBy(xpath = "//input[@name='search']/../../*[local-name()='svg']")
	private WebElement searchIcon;
	
	@FindBy(xpath = "//input[@name='tcs']/following-sibling::div")
	private WebElement agreeTerms;
	
	@FindBy(xpath = "//button[text()='Download Ride-Along Image']")
	private WebElement downloadRideAlongImage;
	
	@FindBy(xpath = "//div[@role='document']/div/div/h1")
	private WebElement downloadSuccessMsg;
	
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


	public void clickFirstPackage() {
		driver.findElement(By.xpath("//div[@type='PACKAGES']/div//img")).click();
	}

	public void agreeToTerms() {
		WebElementUtils.SLEEP(10000);
		new WebDriverWait(driver, WaitTime.SHORT_TO).until(ExpectedConditions.visibilityOf(agreeTerms));
		agreeTerms.click();
	}


	public void clickDownloadRideAlongImage() {
		new WebDriverWait(driver, WaitTime.SHORT_TO).until(ExpectedConditions.elementToBeClickable(downloadRideAlongImage));
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", downloadRideAlongImage);
	}


	public String getDownloadSuccessMsg() {
		return downloadSuccessMsg.getText();
	}
}
