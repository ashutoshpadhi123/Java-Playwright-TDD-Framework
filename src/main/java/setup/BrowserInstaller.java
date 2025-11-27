package setup;

import com.microsoft.playwright.*;

public class BrowserInstaller {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            System.out.println("Playwright browsers installed successfully!");
        }
    }
}