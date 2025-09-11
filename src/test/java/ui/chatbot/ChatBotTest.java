
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

public class ChatBotTest extends BaseTest {

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
    //URLs from CSV to tests
    @DataProvider(name = "chatbotUrls")
    public Object[][] provideUrls() throws Exception {
        return getCsvUrls();
    }
    @Test(dataProvider = "chatbotUrls", priority = 2)
    public void testSites(String urls) throws InterruptedException {
        driver.get(urls);
        ChatBotPage page = new ChatBotPage(driver);

        page.handleCookies();
        page.clickChatBot(urls);
        page.expand();
        int suggestions = page.getDefaultSuggestionsCount();
        Assert.assertEquals(suggestions, 3, "Suggestions mismatch for " + urls);
        page.scheduleMeeting();
        page.getLastReply();
        page.greetingReply();
        page.clickRandomSuggestions();
        page.close();
    }

}
