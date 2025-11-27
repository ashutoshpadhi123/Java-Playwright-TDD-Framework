package framework.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;

public class ExtentManager {
    private static ExtentReports extent;

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {

            String reportFolder = "reports";
            File folder = new File(reportFolder);
            if (!folder.exists()) {
                folder.mkdirs();   // <-- Creates folder automatically
            }

            String reportPath = reportFolder + "/extent-report.html";
            ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
            reporter.config().setDocumentTitle("Playwright Test Report");
            reporter.config().setReportName("UI Automation Report");


            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }
        return extent;
    }
}
