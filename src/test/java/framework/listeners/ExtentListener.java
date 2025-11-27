package framework.listeners;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.*;
import framework.base.BaseTest;
import com.microsoft.playwright.Page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExtentListener implements ITestListener {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter reporter = new ExtentSparkReporter("reports/extent-report.html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @Override
    public void onTestStart(ITestResult result) {
        test.set(extent.createTest(result.getMethod().getMethodName()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail(result.getThrowable());
        Page page = BaseTest.getCurrentPage();

        if (page != null) {
            try {
                Path screenshotDir = Paths.get("screenshots");
                if (!Files.exists(screenshotDir)) {
                    Files.createDirectories(screenshotDir);
                }

                String fileName = result.getMethod().getMethodName() + ".png";
                Path screenshotPath = screenshotDir.resolve(fileName);

                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(screenshotPath)
                        .setFullPage(true));

                test.get().addScreenCaptureFromPath(screenshotPath.toString());

            } catch (IOException e) {
                System.err.println("Error saving screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
