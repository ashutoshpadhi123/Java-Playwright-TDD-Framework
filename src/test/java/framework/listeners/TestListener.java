package framework.listeners;

import com.aventstack.extentreports.Status;
import framework.reports.ExtentManager;
import framework.reports.ExtentTestManager;
import framework.base.BaseTest;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;

public class TestListener extends BaseTest implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTestManager.startTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // 🔹 FORCE CREATE reports folder
        String reportDir = "reports";
        File folder = new File(reportDir);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            System.out.println("Reports folder created: " + created);
        } else {
            System.out.println("Reports folder exists.");
        }

        // Screenshot capture
        String screenshotPath = takeScreenshot(result.getMethod().getMethodName());
        ExtentTestManager.getTest().fail("Test Failed")
                .addScreenCaptureFromPath(screenshotPath);
        ExtentTestManager.getTest().log(Status.FAIL, result.getThrowable().getMessage());
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {
        String reportPath = new File("reports/extent-report.html").getAbsolutePath();
        System.out.println("Flushing Extent Report...");
        System.out.println("Report generated at: " + reportPath);
        ExtentManager.getInstance().flush();
    }
}