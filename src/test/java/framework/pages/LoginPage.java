package framework.pages;

import com.microsoft.playwright.Page;
import framework.utils.ConfigLoader;

public class LoginPage {

    private final Page page;

    // Locators
    private final String username = "input[name='username']";
    private final String password = "input[name='password']";
    private final String loginBtn = "button[type='submit']";
    private final String errorMessage = ".oxd-alert-content-text";

    public LoginPage(Page page) {
        this.page = page;
    }

    public void navigateTo() {
        String baseUrl = ConfigLoader.get("BASE_URL", "https://opensource-demo.orangehrmlive.com/");
        page.navigate(baseUrl);
    }

    public void login(String user, String pass) {
        page.fill(username, user);
        page.fill(password, pass);
        page.click(loginBtn);
    }

    public void loginWithDefaultCredentials() {
        String user = ConfigLoader.get("LOGIN_USER", "Admin");
        String pass = ConfigLoader.get("LOGIN_PASS", "admin123");
        login(user, pass);
    }

    public String getErrorMessage() {
        return page.textContent(errorMessage);
    }
}
