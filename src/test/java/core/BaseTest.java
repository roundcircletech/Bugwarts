package core;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static constants.Messages.DRIVER_INIT_FAILED;
import static constants.Messages.TEARDOWN_ERROR;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        try {
            driver = DriverManager.getDriver();
        } catch (Exception e) {
            System.err.println(DRIVER_INIT_FAILED + e.getMessage());
            throw e;
        }
    }

    @AfterMethod
    public void tearDown() {
        try {
            DriverManager.quitDriver();
        } catch (Exception e) {
            System.err.println(TEARDOWN_ERROR + e.getMessage());
        }
    }
}
