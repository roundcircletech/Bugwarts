package actions;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static constants.CssSelectors.*;
import static constants.CssSelectors.AGENT_REPLY;
import static constants.Strings.*;
import static org.testng.Assert.assertTrue;

public class chatActions {
    public static String getLastReply(WebDriver driver,SearchContext root ) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));
        List<WebElement> botReply = wait.until(d -> {
            List<WebElement> list = root.findElements(By.cssSelector(TEXT_CONATINER_MODULE));
            return list.isEmpty() ? null : list;
        });

        Thread.sleep(4000);
        WebElement lastReply = botReply.get(botReply.size() - 1);
        String direct = lastReply.getText().trim();
        if (!direct.isEmpty()) {
            System.out.println("Bot Reply: " + direct);
        }
        return direct;
    }

    public static String greetingReply(SearchContext root) throws InterruptedException {

        int initialCount = root.findElements(By.cssSelector(TEXT_CONTAINER)).size();

        WebElement chatInput = root.findElement(By.cssSelector(CHAT_INPUT));
        chatInput.click();
        chatInput.sendKeys(INITIAL_GREETINGS_MESSAGE);
        root.findElement(By.cssSelector(SEND_BUTTON)).click();
        Thread.sleep(4000);

        List<WebElement> botReply = root.findElements(By.cssSelector(AGENT_REPLY));

        WebElement lastReply = botReply.get(botReply.size() - 1);

        String direct = lastReply.getText().trim();
        System.out.println("Bot Reply after greeting: " + direct);

        return direct;
    }

    // Count the default chatbot suggestions (buttons under input box)
    public static int getDefaultSuggestionsCount(SearchContext shadowRoot) {
        try {
            // Look for suggestion buttons inside container
            List<WebElement> items = (shadowRoot.findElements(By.cssSelector(SUGGESTIVE_QUESTIONS_CONTAINER)));

            System.out.println("Found " + (shadowRoot.findElements(By.cssSelector(SUGGESTIVE_QUESTIONS_CONTAINER))).size() + " for suggestive responses");
            return (shadowRoot.findElements(By.cssSelector(SUGGESTIVE_QUESTIONS_CONTAINER))).size()
        } catch (Exception e) {
            System.out.println("Couldn't count suggestions: " + e.getMessage());
            return 0;
        }
    }
    public static void verifyChatBotVisible(String url, WebDriver driver, WebElement shadowHost) throws InterruptedException {
        driver.get(url);
        WebElement chatButton = shadowHost.getShadowRoot()
                .findElement(By.cssSelector(SDK_CHAT_BUTTON));
        assertTrue(chatButton.isDisplayed(), "Chatbot is not visible for " + url);
    }


    public static void closeChatBot(SearchContext shadowRoot, String closeChatbotSelector) {
        reusableMethods.clickElement(shadowRoot,closeChatbotSelector, CHATBOT_CLOSED);
    }

    public static void scheduleMeeting(SearchContext shadowRoot, String scheduleSelector) {
        reusableMethods.clickElement(shadowRoot, scheduleSelector, SCHEDULED_MEETING_LINK_CLICKED);
    }

    public static void clickChatBot(String url, SearchContext shadowRoot, String chatSelector) {
        reusableMethods.clickElement(shadowRoot, chatSelector, "Chatbot opened for: " + url);
    }

    public static void expandChatBot(SearchContext shadowRoot, String sdkExpandSelector) {
        reusableMethods.clickElement(shadowRoot, sdkExpandSelector, CHATBOT_EXPANDED);
    }
}
