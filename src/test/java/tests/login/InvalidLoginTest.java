package tests.login;

import framework.base.BaseTest;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InvalidLoginTest extends BaseTest {

    @Test
    public void loginWithInvalidCredentials_shouldShowError() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigateTo();
        // Intentionally wrong credentials
        loginPage.login("wrongUser", "wrongPass");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(
            error != null && error.contains("Invalid credentials"),
            "Expected 'Invalid credentials' error message, but got: " + error
        );
    }
}
