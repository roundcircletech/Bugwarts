package core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static constants.Selectors.HOST_COMPONENT;
import static constants.TimeoutConfig.SHADOW_ROOT_TIMEOUT;

public class Shadow {

    public static SearchContext getRoot(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(SHADOW_ROOT_TIMEOUT))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(d -> d.findElement(By.cssSelector(HOST_COMPONENT)).getShadowRoot());
    }

    public static WebElement find(SearchContext root, String css) {
        return root.findElement(By.cssSelector(css));
    }

    public static List<WebElement> findAll(SearchContext root, String css) {
        return root.findElements(By.cssSelector(css));
    }
}
