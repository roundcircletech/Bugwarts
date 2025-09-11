package core;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class Shadow {

    public static SearchContext getRoot(WebDriver driver) {
        WebElement host = driver.findElement(By.cssSelector("my-component"));
        return host.getShadowRoot();
    }

    public static WebElement find(SearchContext root, String css) {
        return root.findElement(By.cssSelector(css));
    }

    public static List<WebElement> findAll(SearchContext root, String css) {
        return root.findElements(By.cssSelector(css));
    }
}