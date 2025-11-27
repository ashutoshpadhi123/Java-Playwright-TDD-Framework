package framework.listeners;

import com.microsoft.playwright.Page;
import framework.base.BaseTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Page page = BaseTest.getCurrentPage();
        if (page == null) {
            return;
        }

        try {
            Path screenshotsDir = Paths.get("screenshots");
            if (!Files.exists(screenshotsDir)) {
                Files.createDirectories(screenshotsDir);
            }

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String fileName = result.getMethod().getMethodName() + "_" + timestamp + ".png";
            Path screenshotPath = screenshotsDir.resolve(fileName);

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(screenshotPath)
                    .setFullPage(true));

            System.out.println("Saved screenshot: " + screenshotPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }

    @Override public void onTestStart(ITestResult result) { }
    @Override public void onTestSuccess(ITestResult result) { }
    @Override public void onTestSkipped(ITestResult result) { }
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) { }
    @Override public void onStart(ITestContext context) { }
    @Override public void onFinish(ITestContext context) { }
}
