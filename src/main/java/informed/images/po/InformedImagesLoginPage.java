package informed.images.po;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import informed.greetings.utils.BasePage;
import informed.greetings.utils.WaitTime;

public class InformedImagesLoginPage extends BasePage {
	private WebDriver driver;

	@FindBy(id = "username")
	private WebElement userName;

	@FindBy(id = "password")
	private WebElement passWord;

	@FindBy(id = "kc-login")
	private WebElement loginBtn;

	public InformedImagesLoginPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void enterUserName(String uName) {
		userName.sendKeys(uName);
		Reporter.log("<br>enterUserName::" + uName, true);
	}

	public void enterPassword(String uPassWord) {
		passWord.sendKeys(uPassWord);
		Reporter.log("<br>enterPassWord::" + uPassWord, true);
	}

	public InformedImagesHomePage clickSignIn() {
		loginBtn.click();
		Reporter.log("<br>clickSignIn", true);
		return new InformedImagesHomePage(driver);
	}

}
