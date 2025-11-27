package framework.base;

import com.microsoft.playwright.Page;
import framework.utils.ConfigLoader;
import framework.utils.PlaywrightFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.nio.file.Paths;

public class BaseTest {

    protected Page page;
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional String browser) {
        if (browser == null) {
            browser = ConfigLoader.get("BROWSER", "chrome");  // default
        }
        page = PlaywrightFactory.init(browser);
        tlPage.set(page);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // 🔹 Take screenshot only if failed (or use ALWAYS screenshot)
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(result.getMethod().getMethodName());
        }

        PlaywrightFactory.close();
        tlPage.remove();
    }
    public String takeScreenshot(String testName) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String screenshotDir = "screenshots/";
            String screenshotPath = screenshotDir + testName + "_" + timestamp + ".png";

            // 🔹 Create directory if it doesn’t exist
            File folder = new File(screenshotDir);
            if (!folder.exists()) {
                folder.mkdirs();
                System.out.println("Created screenshot folder: " + folder.getAbsolutePath());
            }

            getCurrentPage().screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(screenshotPath))
                    .setFullPage(true));  // full-page screenshot

            System.out.println("Screenshot saved: " + screenshotPath);
            return screenshotPath;
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    public static Page getCurrentPage() {
        return tlPage.get();
    }
}
