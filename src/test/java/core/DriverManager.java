package core;

import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import static constants.Strings.BROWSER_PROPERTY;
import static constants.Strings.CHROME;
import static constants.Strings.SELENIUM_GRID_URL_PROPERTY;
import static constants.Strings.CHROME_DISABLE_AUTOMATION;
import static constants.Strings.CHROME_DISABLE_GPU;
import static constants.Strings.CHROME_OPTION_DISABLE_SHM;
import static constants.Strings.CHROME_OPTION_HEADLESS;
import static constants.Strings.CHROME_OPTION_NO_SANDBOX;
import static constants.Strings.CHROME_OPTION_WINDOW_SIZE;
import static constants.Strings.CHROME_USER_AGENT;
import static constants.Strings.FIREFOX;
import static constants.Strings.FIREFOX_OPTION_HEADLESS;
import static constants.Strings.FIREFOX_OPTION_HEIGHT;
import static constants.Strings.FIREFOX_OPTION_WIDTH;
import static constants.TimeoutConfig.DEFAULT_TIMEOUT;

public class DriverManager {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            String browser = System.getProperty(BROWSER_PROPERTY, CHROME).toLowerCase();
            switch (browser) {
                case CHROME:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    
                    // Headless mode for CI
                    chromeOptions.addArguments(CHROME_OPTION_HEADLESS);
                    chromeOptions.addArguments(CHROME_OPTION_WINDOW_SIZE);
                    chromeOptions.addArguments(CHROME_OPTION_NO_SANDBOX);
                    chromeOptions.addArguments(CHROME_OPTION_DISABLE_SHM);
                    chromeOptions.addArguments(CHROME_DISABLE_GPU);
                    
                    // Anti-detection: realistic user agent + hide automation
                    chromeOptions.addArguments(CHROME_USER_AGENT);
                    chromeOptions.addArguments(CHROME_DISABLE_AUTOMATION);
                    chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                    chromeOptions.setExperimentalOption("useAutomationExtension", false);
                    
                    chromeOptions.setAcceptInsecureCerts(true);
                    driver = createDriver(chromeOptions);
                    break;

                case FIREFOX:
                default:
                    FirefoxOptions firefoxOptions = new FirefoxOptions();

                    firefoxOptions.addArguments(FIREFOX_OPTION_HEADLESS);
                    firefoxOptions.addArguments(FIREFOX_OPTION_WIDTH, FIREFOX_OPTION_HEIGHT);
                    firefoxOptions.setAcceptInsecureCerts(true);
                    driver = createDriver(firefoxOptions);
                    break;
            }
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_TIMEOUT));
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private static WebDriver createDriver(ChromeOptions options) {
        return connect(options, () -> new ChromeDriver(options));
    }

    private static WebDriver createDriver(FirefoxOptions options) {
        return connect(options, () -> new FirefoxDriver(options));
    }

    private static WebDriver connect(Capabilities options, java.util.function.Supplier<WebDriver> localDriver) {
        String gridUrl = System.getProperty(SELENIUM_GRID_URL_PROPERTY);
        if (gridUrl == null || gridUrl.isBlank()) {
            return localDriver.get();
        }
        try {
            return new RemoteWebDriver(new URL(gridUrl), options);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to connect to Selenium grid at " + gridUrl, e);
        }
    }
}
