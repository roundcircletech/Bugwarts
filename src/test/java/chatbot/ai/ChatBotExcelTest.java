package chatbot.ai;

import com.opencsv.CSVReader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
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
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
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
            reader.readNext(); // skipping the header
            while ((row = reader.readNext()) != null) {
                if (row.length > 1 && row[1] != null && !row[1].trim().isEmpty()) {
                    urls.add(row[1].trim()); // using column "URL"
                }
            }
        }
        Object[][] data = new Object[urls.size()][1];
        for (int i = 0; i < urls.size(); i++) data[i][0] = urls.get(i);
        return data;
    }
    //URLs from Excel to tests
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
