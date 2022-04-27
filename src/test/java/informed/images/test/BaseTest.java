package informed.images.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import informed.images.utils.PropertiesLoader;

public abstract class BaseTest {
	private enum DriverType {
		Firefox, IE, Chrome
	}

	public WebDriver driver;
	public String informedGreetingsURL;
	public String informedImagesURL;
	public static Properties configProperties;

	@BeforeClass
	@Parameters({ "driverType" })
	public void beforeMainClass(String driverType) throws URISyntaxException, IOException {
		String projectDir= Paths.get(System.getProperty("user.dir")).getParent().toString();
		String downloadFilepath = projectDir+"\\testDir\\Downloads";
		
		// Load Properties File
		configProperties = PropertiesLoader.loadPropertiesFile(new File(
				projectDir + "\\src\\test\\resources\\config.properties"));
		
		File directory = new File(downloadFilepath);
		if (!directory.exists())
			directory.mkdirs();
		else
			FileUtils.cleanDirectory(directory);
		
		if (DriverType.Firefox.toString().equals(driverType)) {
			String pathToDriver = new File(this.getClass().getResource("/geckodriver.exe").toURI()).toString();
			System.setProperty("webdriver.firefox.marionette", pathToDriver);
			driver = new FirefoxDriver();
		} else if (DriverType.IE.toString().equals(driverType)) {
			String pathToDriver = new File(this.getClass().getResource("/IEDriverServer.exe").toURI()).toString();
			System.setProperty("webdriver.ie.driver", pathToDriver);
			driver = new InternetExplorerDriver();
		} else if (DriverType.Chrome.toString().equals(driverType)) {
			String pathToDriver = new File(this.getClass().getResource("/chromedriver.exe").toURI()).toString();
			System.out.println("pathToDriver" + pathToDriver);
			System.setProperty("webdriver.chrome.driver", pathToDriver);

			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", downloadFilepath);
			chromePrefs.put("download.prompt_for_download", "false");
			
			ChromeOptions ops = new ChromeOptions();
			ops.setExperimentalOption("prefs", chromePrefs);
			ops.addArguments("disable-infobars");
			ops.addArguments("--disable-extensions");
			
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(ChromeOptions.CAPABILITY, ops);
			capabilities.acceptInsecureCerts();
			driver = new ChromeDriver(capabilities);
		} else {
			String pathToDriver = new File(this.getClass().getResource("/geckodriver.exe").toURI()).toString();
			System.setProperty("webdriver.firefox.marionette", pathToDriver);
			driver = new FirefoxDriver();
		}
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		informedGreetingsURL = configProperties.getProperty("informedgreetings.url").toString();
		informedImagesURL = configProperties.getProperty("informedimages.url").toString();
	}

	@AfterClass
	public void afterMainClass() {

	}

	@BeforeTest
	public void beforeMainTest() {

	}

	@AfterTest
	public void afterMainTest() {
		// driver.close();
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		Reporter.log("<br>****************************************************************************************");
		Reporter.log("<br>****************************************************************************************");
		Reporter.log("<br>$$$$$$$$$$$$$$$$$$$$$              " + method.getName() + "       $$$$$$$$$$$$$$$$$$$$$$$$$");
		Reporter.log("<br>****************************************************************************************");
		Reporter.log("<br>****************************************************************************************");
	}

	@AfterMethod
	public void afterMethod(Method method) {
		Reporter.log(
				"<br>XXXXXXXXXXXXXXXXXXXXXXX             " + "-E---N---D-" + "             XXXXXXXXXXXXXXXXXXXXXX");
		Reporter.log("<br>X");
		Reporter.log("<br>X");
		Reporter.log("<br>X");
	}

	public Map<String, Object> pupulateUniqueData(Map<String, Object> data) {
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		String uid = "";
		Random random = new Random();
		for (int i = 0; i < 8; i++) {
			char c = alphabet.charAt(random.nextInt(26));
			uid += c;
		}
		for (Entry<String, Object> entry : data.entrySet()) {
			if (entry.getValue().toString().contains("UNIQUE")) {
				data.put(entry.getKey(), entry.getValue().toString().replaceAll("UNIQUE", uid));
			} else {
				data.put(entry.getKey(), entry.getValue().toString());
			}
		}
		return data;
	}

}
