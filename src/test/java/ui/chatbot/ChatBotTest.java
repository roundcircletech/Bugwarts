
package ui.chatbot;

import pages.ChatBotPage;
import com.opencsv.CSVReader;
import core.BaseTest;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static constants.DataProviders.CHATBOT_URLS;
import static constants.Urls.CLIENT_DETAILS_CSV;
import static constants.Messages.TEST_FAILED_FOR;

public class ChatBotTest extends BaseTest {

    @DataProvider(name = CHATBOT_URLS)
    public Object[][] provideUrls() throws Exception {
        List<String> urls = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(CLIENT_DETAILS_CSV))) {
            reader.readNext(); // skip header
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length > 1 && row[1] != null && !row[1].trim().isEmpty()) {
                    urls.add(row[1].trim());
                }
            }
        }
        return urls.stream().map(url -> new Object[]{url}).toArray(Object[][]::new);
    }
    @Test(dataProvider = CHATBOT_URLS, priority = 2)
    public void testSites(String url) throws InterruptedException {
        try {
            driver.get(url);
            ChatBotPage page = new ChatBotPage(driver);

            page.handleCookies();
            page.clickChatBot(url);
            page.expand();
            
            int suggestions = page.getDefaultSuggestionsCount();
            Assert.assertEquals(suggestions, 3, "Suggestions mismatch for " + url);
            
            page.scheduleMeeting();
            page.getLastReply();
            page.greetingReply();
            page.testInvalidEmail();
            page.clickRandomSuggestions();
            page.close();
        } catch (Exception e) {
            System.err.println(TEST_FAILED_FOR + url + ": " + e.getMessage());
            throw e;
        }
    }

}