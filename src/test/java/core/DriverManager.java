package core;

import org.apache.poi.ss.formula.atp.Switch;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.Locale;

public class DriverManager {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            String browser =  System.getProperty("browser","firefox").toLowerCase();
            switch (browser){
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless=new"); 
                    chromeOptions.addArguments("--window-size=1920,1080");
                    driver = new ChromeDriver(chromeOptions);
                    break;

                case "firefox":
                default:
                    FirefoxOptions options = new FirefoxOptions();
                    options.addArguments("--headless");
                    options.addArguments("--width=1920","--height=1080");
                    driver = new FirefoxDriver(options);
                    break;
            }
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
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
