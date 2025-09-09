package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

public class Waits {

    public static <T> T until(WebDriver driver, Function<WebDriver, T> condition, int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(condition);
    }
}
