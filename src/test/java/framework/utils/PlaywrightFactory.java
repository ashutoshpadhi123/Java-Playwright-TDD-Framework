package framework.utils;

import com.microsoft.playwright.*;

import java.util.Arrays;

/**
 * Thread-safe Playwright factory for parallel TestNG execution.
 */
public class PlaywrightFactory {

    private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();

    public static Page init(String browserName) {
        System.setProperty("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1");
        // Create Playwright instance
        Playwright playwright = Playwright.create();

        // Select browser based on input
        Browser browser;

        switch (browserName.toLowerCase()) {
            case "firefox":
                browser = playwright.firefox().launch(
                        new BrowserType.LaunchOptions().setHeadless(false)
                );
                break;

            case "webkit":  // Safari engine
                browser = playwright.webkit().launch(
                        new BrowserType.LaunchOptions().setHeadless(false)
                );
                break;

            default: // Chrome FULLSCREEN
                browser = playwright.chromium().launch(
                        new BrowserType.LaunchOptions()
                                .setChannel("chrome")          // real Chrome
                                .setHeadless(false)            // GUI mode only
                                .setArgs(Arrays.asList("--start-maximized", "--disable-gpu"))
                );
                break;
        }

        BrowserContext context = browser.newContext(
                new Browser.NewContextOptions().setViewportSize(null)  // allow full size
        );

        Page page = context.newPage();
        return page;
    }

    public static Page getPage() {
        return tlPage.get();
    }

    public static void close() {
        Page page = tlPage.get();
        if (page != null) {
            page.close();
        }

        Browser browser = tlBrowser.get();
        if (browser != null) {
            browser.close();
        }

        Playwright playwright = tlPlaywright.get();
        if (playwright != null) {
            playwright.close();
        }

        tlPage.remove();
        tlBrowser.remove();
        tlPlaywright.remove();
    }
}
