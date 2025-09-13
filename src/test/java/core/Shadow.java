package core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Shadow {

    public static SearchContext getRoot(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(d -> d.findElement(By.cssSelector("my-component")).getShadowRoot());
    }

    public static WebElement find(SearchContext root, String css) {
        return root.findElement(By.cssSelector(css));
    }

    public static List<WebElement> findAll(SearchContext root, String css) {
        return root.findElements(By.cssSelector(css));
    }
}
