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

            Thread.sleep(9000);
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

    private List<WebElement> waitForVisibleSuggestions(Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < end) {
            try {
                SearchContext r = Shadow.getRoot(driver);
                List<WebElement> list = r.findElements(BY_SUGGEST);
                list.removeIf(e -> {
                    try { return !e.isDisplayed() || e.getText().trim().isEmpty(); }
                    catch (Exception ex) { return true; }
                });
                if (!list.isEmpty()) return list;
            } catch (StaleElementReferenceException | NoSuchElementException ignored) {}
            try { Thread.sleep(120); } catch (InterruptedException ignored) {}
        }
        return new java.util.ArrayList<>();
    }

    private String suggestionSignature() {
        try {
            SearchContext r = Shadow.getRoot(driver);
            List<WebElement> list = r.findElements(BY_SUGGEST);
            StringBuilder sb = new StringBuilder();
            for (WebElement e : list) {
                try {
                    if (e.isDisplayed()) sb.append(e.getText().trim()).append("|");
                } catch (Exception ignored) {}
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private List<WebElement> waitForSuggestionsChange(String oldSig, long maxWaitMs) {
        long end = System.currentTimeMillis() + maxWaitMs;
        while (System.currentTimeMillis() < end) {
            String sig = suggestionSignature();
            if (!sig.isEmpty() && !sig.equals(oldSig)) {
                return waitForVisibleSuggestions(Duration.ofSeconds(5));
            }
            try { Thread.sleep(150); } catch (InterruptedException ignored) {}
        }
        return new java.util.ArrayList<>();
    }

    private int getCurrentBotReplyCount() {
        SearchContext r = Shadow.getRoot(driver);
        return r.findElements(BY_AGENT_TEXT).size();
    }

    private String waitForNewBotReplyStable(int oldCount, long quietMs, long maxWaitMs) {
        long start = System.currentTimeMillis();
        long lastChange = start;
        String last = "";
        WebElement latest = null;

        while (System.currentTimeMillis() - start < maxWaitMs) {
            try {
                SearchContext r = driver.findElement(BY_HOST).getShadowRoot();
                List<WebElement> bubbles = r.findElements(BY_AGENT_TEXT);

                if (bubbles.size() > oldCount) {
                    latest = bubbles.get(bubbles.size() - 1);
                    String cur = (String) ((JavascriptExecutor) driver)
                            .executeScript("return arguments[0].innerText || arguments[0].textContent || '';", latest);
                    cur = (cur == null) ? "" : cur.trim();

                    if (!cur.equals(last)) {
                        last = cur;
                        lastChange = System.currentTimeMillis();
                    } else if (System.currentTimeMillis() - lastChange >= quietMs) {
                        return last;
                    }
                }
            } catch (StaleElementReferenceException | NoSuchElementException ignored) {}
            try { Thread.sleep(120); } catch (InterruptedException ignored) {}
        }
        return last;
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
        List<WebElement> sugs = waitForVisibleSuggestions(Duration.ofSeconds(45));
        if (sugs.isEmpty()) {
            System.out.println("No suggestions visible initially, skipping.");
            return;
        }
        System.out.println("Found " + sugs.size() + " suggestive responses");

        int clicks = Math.min(3, sugs.size());
        for (int i = 0; i < clicks; i++) {
            sugs = waitForVisibleSuggestions(Duration.ofSeconds(30));
            if (sugs.isEmpty() || i >= sugs.size()) {
                System.out.println("No suggestions to click now, stopping.");
                break;
            }

            WebElement choice = sugs.get(i);
            String label = safeText(choice);
            System.out.println("Click -> " + label);

            int beforeReplies = getCurrentBotReplyCount();
            String oldSig = suggestionSignature();

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", choice);
            easyClick(choice);

            String full = waitForNewBotReplyStable(beforeReplies, 900, 25000);
            if (!full.isEmpty()) System.out.println("Bot: " + full);

            List<WebElement> next = waitForSuggestionsChange(oldSig, 20000);
            if (next.isEmpty()) {
                System.out.println("No next suggestions yet (stopping to avoid timeout).");
                break;
            }

            Thread.sleep(500);
        }
    }
}
