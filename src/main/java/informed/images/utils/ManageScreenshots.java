package informed.images.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;

public class ManageScreenshots {

	public static void takeScreenShot(ITestResult result,WebDriver driver, String testName) {
        try {
            File ssFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
           String path = System.getProperty("user.dir") + "/SeleniumSS/"+testName+".png";
           FileUtils.copyFile(ssFile, new File(path));
           Reporter.setCurrentTestResult(result);
           Reporter.log("<br><a href='"+new File(path)+"' target='_blank'><img target='_blank' src='"+new File(path)+"' height='100' width='100' /></a><br>");
        }catch (Exception e) {
            Reporter.log("<br>Exception while taking screenshot from Test case: " + testName, true);
            Reporter.log(e.getMessage());
        }
    }
}
