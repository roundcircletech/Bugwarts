package chatbot.ai;

import com.opencsv.CSVReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.FileReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ChatBotExcelTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // If geckodriver is not in PATH, set it explicitly:
        // System.setProperty("webdriver.gecko.driver", "path/to/geckodriver");

        FirefoxOptions options = new FirefoxOptions();

        // Window size
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");

        // Set a custom user agent
        options.addPreference("general.useragent.override",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36");

        // Useful Firefox preferences
        options.addPreference("dom.security.https_only_mode", false);
        options.addPreference("dom.disable_open_during_load", false);
        options.addPreference("media.autoplay.default", 0);

        // Launch Firefox
        driver = new FirefoxDriver(options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        // If you don’t want fixed size, you can maximize instead:
        // driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private Object[][] getCsvUrls() throws Exception {
        List<String> urls = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("src/test/resources/clientDetails.csv"))) {
            String[] row;
            reader.readNext(); // skip header
            while ((row = reader.readNext()) != null) {
                if (row.length > 1 && row[1] != null && !row[1].trim().isEmpty()) {
                    urls.add(row[1].trim());
                }
            }
        }
        Object[][] data = new Object[urls.size()][1];
        for (int i = 0; i < urls.size(); i++) {
            data[i][0] = urls.get(i);
        }
        return data;
    }

    @DataProvider(name = "chatbotUrls")
    public Object[][] provideUrls() throws Exception {
        return getCsvUrls();
    }

    @Test(dataProvider = "chatbotUrls", priority = 2)
    public void testSites(String urls) throws InterruptedException {
        driver.get(urls);
        ChatBotActions.handleCookiesIfPresent(driver);
        ChatBotActions.clickChatBot(urls, driver);
        ChatBotActions.expandChatBot(driver);
        int suggestions = ChatBotActions.getDefaultSuggestionsCount(driver);
        Assert.assertEquals(suggestions, 3, "Suggestions mismatch for " + urls);
        ChatBotActions.scheduleMeeting(driver);
        ChatBotActions.getLastReply(driver);
        ChatBotActions.greetingReply(driver);
        ChatBotActions.clickRandomSuggestion(driver);
        ChatBotActions.closeChatBot(driver);
    }
}
