package pages;

import core.Shadow;
import core.Waits;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ChatBotPage {

    private WebDriver driver;
    private SearchContext root;

    public ChatBotPage(WebDriver driver) {
        this.driver = driver;
        this.root = Shadow.getRoot(driver);
    }

    public void handleCookies() {
        try {
            driver.findElement(By.xpath("//div[contains(text(),'Decline')]")).click();
            System.out.println("Cookies Rejected");
        } catch (NoSuchElementException e1) {
            try {
                driver.findElement(By.xpath("//div[contains(text(),'Allow Cookies')]")).click();
                System.out.println("Cookies Accepted");
            } catch (NoSuchElementException e2) {
                System.out.println("No Cookies Found");
            }
        }
    }

    public void clickChatBot(String url) {
        try {
            Shadow.find(root, "button.ChatButton-module_sdkChatButton__M1mKI").click();
            System.out.println("Chatbot opened for: " + url);
        } catch (Exception e) {
            System.out.println("Couldn't open chatbot for: " + url + " | " + e.getMessage());
        }
    }

    public void expand() {
        try {
            Shadow.find(root, "button.ChatHeader-module_sdkExpandButton__qtONk").click();
            System.out.println("Chatbot Expanded");
        } catch (Exception e) {
            System.out.println("Couldn't expand chatbot: " + e.getMessage());
        }
    }

    public void close() {
        try {
            Shadow.find(root, "button.ChatHeader-module_sdkCloseButton__AQvQv").click();
            System.out.println("Chatbot closed");
        } catch (Exception e) {
            System.out.println("Couldn't close chatbot: " + e.getMessage());
        }
    }

    public int getDefaultSuggestionsCount() {
        try {
            List<WebElement> items = Shadow.findAll(root, "div[class*='sdkSuggestedQuestionsContainer'] button");
            System.out.println("Found " + items.size() + " for suggestive responses");
            return items.size();
        } catch (Exception e) {
            System.out.println("Couldn't count suggestions: " + e.getMessage());
            return 0;
        }
    }

    public void scheduleMeeting() {
        try {
            Shadow.find(root, "button.ChatInputBox-module_sdkCalendarButton__r3Rt3").click();
            Thread.sleep(2000);
            System.out.println("Schedule Meeting Clicked");
        } catch (Exception e) {
            System.out.println("Couldn't find the schedule meeting button: " + e.getMessage());
        }
    }

    public String getLastReply() throws InterruptedException {
        List<WebElement> botReply = Waits.until(driver, d -> {
            List<WebElement> list = Shadow.findAll(root, "div[class*='AiText-module_textContainer']");
            return list.isEmpty() ? null : list;
        }, 12);

        WebElement lastReply = botReply.get(botReply.size() - 1);
        String text = lastReply.getText().trim();
        if (!text.isEmpty()) {
            System.out.println("Bot Reply: " + text);
        }
        return text;
    }

    public String greetingReply() throws InterruptedException {
        WebElement chatInput = Shadow.find(root, "textarea[role='textbox'][aria-label='Chat input']");
        chatInput.click();
        chatInput.sendKeys("Hi");

        Shadow.find(root, "button.ChatInputBox-module_sdkSendButton__CLWm6").click();
        Thread.sleep(2000);

        List<WebElement> botReply = Shadow.findAll(root, "div[class*='AiText-module_textContainer']");
        WebElement lastReply = botReply.get(botReply.size() - 1);
        String reply = lastReply.getText().trim();
        System.out.println("Bot Reply after greeting: " + reply);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> Shadow.findAll(root, "h1[class*='suggestiveResponse']").size() >= 3);

        List<WebElement> greetSuggestions = Shadow.findAll(root, "h1[class*='suggestiveResponse']");
        System.out.println("Suggestions after greeting: " + greetSuggestions.size());

        return reply;
    }

    public void clickRandomSuggestions() throws InterruptedException {
        // Shadow root
        SearchContext root = core.Shadow.getRoot(driver);

        String sug = "h1[class*='suggestiveResponse']";
        String reply = "div[class*='AiText-module_textContainer']";

        for (int i = 0; i < 3; i++) {
            List<WebElement> suggestions = core.Shadow.findAll(root, sug);
            if (i >= suggestions.size()) {
                break;
            }

            WebElement choice = suggestions.get(i);
            String label = "";

            try {
                label = choice.getText().trim();
            } catch (StaleElementReferenceException e) {
                // if element goes stale, refreshing the "root and suggestions"
                root = core.Shadow.getRoot(driver);
                suggestions = core.Shadow.findAll(root, sug);
                if (i < suggestions.size()) {
                    choice = suggestions.get(i);
                    label = choice.getText().trim();
                }
            }

            System.out.println("Clicking suggestion -> " + label);

            int beforeCount = core.Shadow.findAll(root, reply).size();

            try {
                choice.click();
            } catch (StaleElementReferenceException e) {
                // retrying once more if it gets failed first time
                root = core.Shadow.getRoot(driver);
                suggestions = core.Shadow.findAll(root, sug);
                if (i < suggestions.size()) {
                    choice = suggestions.get(i);
                    choice.click();
                }
            }
            // waiting for a new reply after click happens
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> core.Shadow.findAll(core.Shadow.getRoot(driver), reply).size() > beforeCount);

            Thread.sleep(3000);
        }
    }
}
