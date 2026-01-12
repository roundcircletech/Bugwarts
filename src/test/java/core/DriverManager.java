package core;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import static constants.Strings.BROWSER_PROPERTY;
import static constants.Strings.CHROME;
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
                    
                    chromeOptions.addArguments(CHROME_OPTION_HEADLESS);
                    chromeOptions.addArguments(CHROME_OPTION_WINDOW_SIZE);
                    chromeOptions.addArguments(CHROME_OPTION_NO_SANDBOX);
                    chromeOptions.addArguments(CHROME_OPTION_DISABLE_SHM);
                    chromeOptions.addArguments(CHROME_USER_AGENT);
                    chromeOptions.setAcceptInsecureCerts(true);
                    driver = new ChromeDriver(chromeOptions);
                    break;

                case FIREFOX:
                default:
                    FirefoxOptions options = new FirefoxOptions();
                    
                    options.addArguments(FIREFOX_OPTION_HEADLESS);
                    options.addArguments(FIREFOX_OPTION_WIDTH, FIREFOX_OPTION_HEIGHT);
                    options.setAcceptInsecureCerts(true);
                    driver = new FirefoxDriver(options);
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
}
