package framework.base;

import com.microsoft.playwright.Page;
import framework.utils.ConfigLoader;
import framework.utils.PlaywrightFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

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
    public void tearDown() {
        PlaywrightFactory.close();
        tlPage.remove();
    }

    public static Page getCurrentPage() {
        return tlPage.get();
    }
}
