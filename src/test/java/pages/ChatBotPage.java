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

    private static final By BY_HOST = By.cssSelector("my-component");
    private static final By BY_CHAT_INPUT = By.cssSelector("textarea[role='textbox'][aria-label='Chat input']");
    private static final By BY_SEND = By.cssSelector("button.ChatInputBox-module_sdkSendButton__CLWm6");
    private static final By BY_AGENT_TEXT = By.cssSelector("div[class*='AiText-module_textContainer']");
    private static final By BY_SUGGEST = By.cssSelector("h1[class*='suggestiveResponse']");

    public ChatBotPage(WebDriver driver) {
        this.driver = driver;
        this.root = Shadow.getRoot(driver);
    }

    public int greetingReply() throws InterruptedException {
        return doGreetingReply(this.driver);
    }

    private static boolean isSchedulerReply(String text) {
        if (text == null) return false;
        String s = text.toLowerCase();
        return s.contains("schedule") || s.contains("email address") || s.contains("name and company");
    }

    private static void waitForMainChatInput(WebDriver driver, Duration timeout) {
        WebDriverWait w = new WebDriverWait(driver, timeout);
        w.ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    SearchContext r = d.findElement(BY_HOST).getShadowRoot();
                    List<WebElement> inputs = r.findElements(BY_CHAT_INPUT);
                    return !inputs.isEmpty() && inputs.get(0).isDisplayed();
                });
    }

    private static String waitAndGetNewAgentReply(WebDriver driver, int previousCount, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        WebElement bubble = wait.until(d -> {
            SearchContext r = d.findElement(BY_HOST).getShadowRoot();
            List<WebElement> msgs = r.findElements(BY_AGENT_TEXT);
            return msgs.size() > previousCount ? msgs.get(msgs.size() - 1) : null;
        });

        String text = "", curr;
        long stableFor = 0;
        while (stableFor < 600) {
            curr = bubble.getText().trim();
            if (!curr.equals(text)) {
                text = curr;
                stableFor = 0;
            } else {
                stableFor += 150;
            }
            try { Thread.sleep(150); } catch (InterruptedException ignored) {}
        }
        return text;
    }

    public void handleCookies() {
        try {
            driver.findElement(By.xpath("//div[contains(text(),'Decline')]")).click();
            System.out.println("Cookies: declined");
        } catch (NoSuchElementException e1) {
            try {
                driver.findElement(By.xpath("//div[contains(text(),'Allow Cookies')]")).click();
                System.out.println("Cookies: accepted");
            } catch (NoSuchElementException e2) {
                System.out.println("Cookies: none");
            }
        }
    }

    public void clickChatBot(String url) {
        try {
            root = Shadow.getRoot(driver);
            Shadow.find(root, "button.ChatButton-module_sdkChatButton__M1mKI").click();
            System.out.println("Chat opened: " + url);
        } catch (Exception e) {
            System.out.println("Open failed: " + url + " | " + e.getMessage());
        }
    }

    public void expand() {
        try {
            root = Shadow.getRoot(driver);
            Shadow.find(root, "button.ChatHeader-module_sdkExpandButton__qtONk").click();
            System.out.println("Expanded");
            Thread.sleep(400);
        } catch (Exception e) {
            System.out.println("Expand failed: " + e.getMessage());
        }
    }

    public void close() {
        try {
            root = Shadow.getRoot(driver);
            Shadow.find(root, "button.ChatHeader-module_sdkCloseButton__AQvQv").click();
            System.out.println("Closed");
        } catch (Exception e) {
            System.out.println("Close failed: " + e.getMessage());
        }
    }

    public int getDefaultSuggestionsCount() {
        try {
            root = Shadow.getRoot(driver);
            List<WebElement> items = Shadow.findAll(root, "div[class*='sdkSuggestedQuestionsContainer'] button");
            System.out.println("Suggestions: " + items.size());
            return items.size();
        } catch (Exception e) {
            System.out.println("Suggestions count failed: " + e.getMessage());
            return 0;
        }
    }

    public void scheduleMeeting() {
        try {
            root = Shadow.getRoot(driver);
            Shadow.find(root, "button.ChatInputBox-module_sdkCalendarButton__r3Rt3").click();
            Thread.sleep(2000);
            System.out.println("Scheduler clicked");
        } catch (Exception e) {
            System.out.println("Scheduler click failed: " + e.getMessage());
        }
    }

    public String getLastReply() throws InterruptedException {
        try {
            List<WebElement> botReply = Waits.until(driver, d -> {
                SearchContext r = Shadow.getRoot(d);
                List<WebElement> list = Shadow.findAll(r, "div[class*='AiText-module_textContainer']");
                return list.isEmpty() ? null : list;
            }, 20);

            Thread.sleep(500);
            WebElement last = botReply.get(botReply.size() - 1);
            String text = last.getText().trim();
            if (!text.isEmpty()) System.out.println("Bot: " + text);
            return text;
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("No bot reply within wait window (continuing)");
            return "";
        }
    }

    private static int doGreetingReply(WebDriver driver) throws InterruptedException {
        waitForMainChatInput(driver, Duration.ofSeconds(10));

        String reply;
        for (int attempt = 0; attempt < 2; attempt++) {
            SearchContext root = driver.findElement(BY_HOST).getShadowRoot();

            int before = root.findElements(BY_AGENT_TEXT).size();

            WebElement chatInput = root.findElement(BY_CHAT_INPUT);
            chatInput.click();
            chatInput.sendKeys("Hi");

            root.findElement(BY_SEND).click();

            Thread.sleep(3000); 
            reply = waitAndGetNewAgentReply(driver, before, Duration.ofSeconds(12));
            System.out.println("Reply: " + reply);

            if (isSchedulerReply(reply) && attempt == 0) {
                System.out.println("Scheduler: retry");
                waitForMainChatInput(driver, Duration.ofSeconds(10));
                continue;
            }
            break;
        }

        SearchContext root = driver.findElement(BY_HOST).getShadowRoot();
        int suggestions = root.findElements(BY_SUGGEST).size();
        if (suggestions == 0) {
            Thread.sleep(600);
            root = driver.findElement(BY_HOST).getShadowRoot();
            suggestions = root.findElements(BY_SUGGEST).size();
        }

        System.out.println("Suggestions: " + suggestions);
        return suggestions;
    }

    private SearchContext R() { return Shadow.getRoot(driver); }

    private static boolean isVisible(WebElement el) {
        try {
            return el != null && el.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private List<WebElement> waitForVisibleSuggestions(Duration timeout) {
        WebDriverWait w = new WebDriverWait(driver, timeout);
        return w.ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    List<WebElement> list = Shadow.findAll(Shadow.getRoot(d), "h1[class*='suggestiveResponse']");
                    list.removeIf(el -> !isVisible(el));
                    return list.isEmpty() ? null : list;
                });
    }

    private void easyClick(WebElement el) {
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private static String safeText(WebElement el) {
        try { return el.getText().trim(); } catch (Exception e) { return ""; }
    }

    public void clickRandomSuggestions() throws InterruptedException {
        final String reply = "div[class*='AiText-module_textContainer']";

        List<WebElement> sugs = waitForVisibleSuggestions(Duration.ofSeconds(45));
        System.out.println("Found " + sugs.size() + " suggestive responses");

        int clicks = Math.min(3, sugs.size());
        for (int i = 0; i < clicks; i++) {
            sugs = waitForVisibleSuggestions(Duration.ofSeconds(15));
            if (i >= sugs.size()) break;

            WebElement choice = sugs.get(i);
            String label = safeText(choice);
            System.out.println("Click -> " + label);

            int before = Shadow.findAll(R(), reply).size();

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", choice);
            easyClick(choice);

            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> Shadow.findAll(Shadow.getRoot(d), reply).size() > before);

            Thread.sleep(700);
        }
    }
}
