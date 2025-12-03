package ui.chatbot;

import pages.ChatBotPage;
import core.BaseTest;
import core.CsvUtils;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.List;

import static constants.DataProviders.CHATBOT_URLS;
import static constants.Urls.CLIENT_DETAILS_CSV;
import static constants.Messages.TEST_FAILED_FOR;

public class ChatBotTest extends BaseTest {

    @DataProvider(name = CHATBOT_URLS)
    public Object[][] provideUrls() throws Exception {
        List<String> urls = CsvUtils.readCsvColumn(CLIENT_DETAILS_CSV, 1, true, true);
        return CsvUtils.toDataProvider(urls);
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
        } catch (InterruptedException | RuntimeException | AssertionError e) {
            System.err.println(TEST_FAILED_FOR + url + ": " + e.getMessage());
            throw e;
        }
    }

}

