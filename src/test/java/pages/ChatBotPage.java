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

    private static boolean isSchedulerReply(String text) {
        if (text == null) return false;
        String s = text.toLowerCase();
        return s.contains("schedule") || s.contains("email address") || s.contains("name and company");
    }

    private static void waitForMainChatInput(WebDriver driver, Duration timeout) {
        new WebDriverWait(driver, Duration.ofSeconds(timeout.getSeconds())).until(d -> {
            SearchContext r = d.findElement(By.cssSelector("my-component")).getShadowRoot();
            return !r.findElements(By.cssSelector("textarea[role='textbox'][aria-label='Chat input']")).isEmpty();
        });
    }

    private static String waitAndGetNewAgentReply(WebDriver driver, int previousCount, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout.getSeconds()));
        return wait.until(d -> {
            SearchContext r = d.findElement(By.cssSelector("my-component")).getShadowRoot();
            List<WebElement> msgs = r.findElements(By.cssSelector("div[class*='AiText-module_textContainer']"));
            if (msgs.size() > previousCount) {
                return msgs.get(msgs.size() - 1).getText();
            }
            return null;
        });
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

        Thread.sleep(3000);
        WebElement lastReply = botReply.get(botReply.size() - 1);
        String text = lastReply.getText().trim();
        if (!text.isEmpty()) {
            System.out.println("Bot Reply: " + text);
        }
        return text;
    }

    public static int greetingReply(WebDriver driver) throws InterruptedException {
        SearchContext root = driver.findElement(By.cssSelector("my-component")).getShadowRoot();

        if (root.findElements(By.cssSelector("textarea[role='textbox'][aria-label='Chat input']")).isEmpty()) {
            System.out.println("Scheduler is open");
        }
        waitForMainChatInput(driver, Duration.ofSeconds(10));
        root = driver.findElement(By.cssSelector("my-component")).getShadowRoot();

        int before = root.findElements(By.cssSelector("div[class*='AiText-module_textContainer']")).size();
        WebElement chatInput = root.findElement(By.cssSelector("textarea[role='textbox'][aria-label='Chat input']"));
        chatInput.click();
        chatInput.sendKeys("Hi");
        root.findElement(By.cssSelector("button.ChatInputBox-module_sdkSendButton__CLWm6")).click();
        Thread.sleep(3000);
        String reply = waitAndGetNewAgentReply(driver, before, Duration.ofSeconds(12));
        System.out.println("Reply after greetings: " + reply);

        if (isSchedulerReply(reply)) {
            System.out.println("Scheduler is open");
            waitForMainChatInput(driver, Duration.ofSeconds(10));

            SearchContext r2 = driver.findElement(By.cssSelector("my-component")).getShadowRoot();
            int beforeRetry = r2.findElements(By.cssSelector("div[class*='AiText-module_textContainer']")).size();

            WebElement chatInput2 = r2.findElement(By.cssSelector("textarea[role='textbox'][aria-label='Chat input']"));
            chatInput2.click();
            chatInput2.sendKeys("Hi");
            r2.findElement(By.cssSelector("button.ChatInputBox-module_sdkSendButton__CLWm6")).click();
            Thread.sleep(3000);
            reply = waitAndGetNewAgentReply(driver, beforeRetry, Duration.ofSeconds(12));
            System.out.println("Reply after greetings: " + reply);
        }

        root = driver.findElement(By.cssSelector("my-component")).getShadowRoot();
        int suggestions = root.findElements(By.cssSelector("h1[class*='suggestiveResponse']")).size();
        if (suggestions == 0) {
            Thread.sleep(600);
            root = driver.findElement(By.cssSelector("my-component")).getShadowRoot();
            suggestions = root.findElements(By.cssSelector("h1[class*='suggestiveResponse']")).size();
        }
        System.out.println("Suggestions after greeting: " + suggestions);

        return suggestions;
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
