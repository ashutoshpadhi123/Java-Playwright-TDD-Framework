package framework.pages;

import com.microsoft.playwright.Page;

public class DashboardPage {

    private final Page page;

    // Example selectors
    private final String dashboardHeader = "//div[@class='oxd-topbar-header']";
    private final String searchInput = "//input[placeholder='Search']";

    public DashboardPage(Page page) {
        this.page = page;
    }

    public boolean isDashboardVisible() {
        return page.waitForSelector(dashboardHeader) != null;
    }

    public void search(String query) {
        if (page.isVisible(searchInput)) {
            page.fill(searchInput, query);
            page.keyboard().press("Enter");
        }
    }
}
