package Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import static constants.Strings.*;

public class CookiesUtils {
    // Handle cookie banners if they appear on the site
    public static String handleCookiesIfPresent(WebDriver driver, String rejectPath, String acceptPath) {
        try {
            driver.findElement(By.xpath(rejectPath)).click();
            return COOKIES_REJECTED;
        } catch (NoSuchElementException e1) {
            try {
                driver.findElement(By.xpath(acceptPath)).click();
                return COOKIES_ACCEPTED;
            } catch (NoSuchElementException e2) {
                return NO_COOKIES_FOUND;
            }
        }
    }
}