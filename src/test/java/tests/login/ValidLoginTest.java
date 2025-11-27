package tests.login;

import framework.base.BaseTest;
import framework.pages.DashboardPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ValidLoginTest extends BaseTest {

    @Test
    public void loginWithValidCredentials_shouldLandOnDashboard() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigateTo();
        loginPage.loginWithDefaultCredentials();

        DashboardPage dashboardPage = new DashboardPage(page);
        Assert.assertTrue(
            dashboardPage.isDashboardVisible(),
            "Dashboard should be visible after successful login"
        );
        dashboardPage.search("Menu");
    }
}
