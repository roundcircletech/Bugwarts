package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static constants.Messages.AGENT_PREFIX;
import static constants.Messages.BROWSER_SESSION_LOST_EMAIL;
import static constants.Messages.BROWSER_SESSION_LOST_GENERIC;
import static constants.Messages.BROWSER_SESSION_LOST_GREETING;
import static constants.Messages.BROWSER_SESSION_LOST_SCHEDULER;
import static constants.Messages.CHAT_ALREADY_OPEN;
import static constants.Messages.CHAT_OPENED;
import static constants.Messages.CLICK_ARROW;
import static constants.Messages.CLOSED;
import static constants.Messages.CLOSE_FAILED;
import static constants.Messages.COOKIES_ACCEPTED;
import static constants.Messages.COOKIES_DECLINED;
import static constants.Messages.COOKIES_NONE;
import static constants.Messages.EXPAND_FAILED;
import static constants.Messages.NO_AGENT_REPLY;
import static constants.Messages.NO_NEXT_SUGGESTIONS;
import static constants.Messages.NO_SUGGESTIONS_NOW;
import static constants.Messages.NO_SUGGESTIONS_VISIBLE;
import static constants.Messages.OPEN_FAILED;
import static constants.Messages.REPLY_PREFIX;
import static constants.Messages.SCHEDULER_CLICKED;
import static constants.Messages.SCHEDULER_CLICK_FAILED;
import static constants.Messages.SCHEDULER_RETRY;
import static constants.Messages.SDK_EXPANDED;
import static constants.Messages.SUGGESTIONS_COUNT_FAILED;
import static constants.Messages.SUGGESTIONS_COUNT_PREFIX;
import static constants.Messages.SUGGESTIONS_FOUND;
import static constants.Messages.SUGGESTIONS_LABEL;
import static constants.Messages.SUGGESTIONS_SUFFIX;
import static constants.Selectors.AGENT_TEXT;
import static constants.Selectors.CALENDAR_BUTTON;
import static constants.Selectors.CHAT_BUTTON;
import static constants.Selectors.CHAT_INPUT;
import static constants.Selectors.CLOSE_BUTTON;
import static constants.Selectors.COOKIE_ALLOW_XPATH;
import static constants.Selectors.COOKIE_DECLINE_XPATH;
import static constants.Selectors.EXPAND_BUTTON;
import static constants.Selectors.HOST_COMPONENT;
import static constants.Selectors.JS_CLICK;
import static constants.Selectors.JS_GET_TEXT;
import static constants.Selectors.JS_SCROLL_INTO_VIEW;
import static constants.Selectors.SEND_BUTTON;
import static constants.Selectors.SUGGESTED_QUESTIONS_CONTAINER;
import static constants.Selectors.SUGGESTIONS;
import static constants.Strings.EMPTY_STRING;
import static constants.Strings.GREETING_MSG;
import static constants.Strings.INVALID_EMAIL;
import static constants.Strings.KEYWORD_EMAIL_ADDRESS;
import static constants.Strings.KEYWORD_NAME_AND_COMPANY;
import static constants.Strings.KEYWORD_SCHEDULE;
import static constants.Strings.PIPE_SEPARATOR;
import static constants.TimeoutConfig.AFTER_EXPAND_DELAY;
import static constants.TimeoutConfig.AFTER_SCHEDULER_DELAY;
import static constants.TimeoutConfig.AFTER_SEND_DELAY;
import static constants.TimeoutConfig.AGENT_REPLY_DELAY;
import static constants.TimeoutConfig.AGENT_REPLY_TIMEOUT;
import static constants.TimeoutConfig.AUTO_POPUP_WAIT;
import static constants.TimeoutConfig.BOT_REPLY_MAX_WAIT;
import static constants.TimeoutConfig.BOT_REPLY_QUIET_TIME;
import static constants.TimeoutConfig.CHAT_INPUT_TIMEOUT;
import static constants.TimeoutConfig.CLICK_DELAY;
import static constants.TimeoutConfig.ELEMENT_CHECK_INTERVAL;
import static constants.TimeoutConfig.MAX_GREETING_RETRIES;
import static constants.TimeoutConfig.MAX_SUGGESTION_CLICKS;
import static constants.TimeoutConfig.PAGE_LOAD_TIMEOUT;
import static constants.TimeoutConfig.SHADOW_ROOT_TIMEOUT;
import static constants.TimeoutConfig.SUGGESTIONS_CHANGE_TIMEOUT;
import static constants.TimeoutConfig.SUGGESTIONS_CHANGE_WAIT;
import static constants.TimeoutConfig.SUGGESTIONS_RECHECK_DELAY;
import static constants.TimeoutConfig.SUGGESTIONS_REFRESH_TIMEOUT;
import static constants.TimeoutConfig.SUGGESTIONS_TIMEOUT;
import static constants.TimeoutConfig.TEXT_CHECK_INTERVAL;
import static constants.TimeoutConfig.TEXT_STABLE_WAIT;
import core.Shadow;
import core.Waits;

public class ChatBotPage {

    private final WebDriver driver;
    private SearchContext root;
    private boolean schedulerClicked = false;

    private static final By BY_HOST = By.cssSelector(HOST_COMPONENT);
    private static final By BY_CHAT_INPUT = By.cssSelector(CHAT_INPUT);
    private static final By BY_SEND = By.cssSelector(SEND_BUTTON);
    private static final By BY_AGENT_TEXT = By.cssSelector(AGENT_TEXT);
    private static final By BY_SUGGEST = By.cssSelector(SUGGESTIONS);

    public ChatBotPage(WebDriver driver) {
        this.driver = driver;
    }

    public void greetingReply() throws InterruptedException {
        try {
            doGreetingReply(this.driver, GREETING_MSG, schedulerClicked);
        } catch (NoSuchSessionException e) {
            System.err.println(BROWSER_SESSION_LOST_GREETING);
            throw e;
        }
    }

    public void testInvalidEmail() throws InterruptedException {
        try {
            String reply = doGreetingReply(this.driver, INVALID_EMAIL, schedulerClicked);
            validateInvalidEmailResponse(reply);
        } catch (NoSuchSessionException e) {
            System.err.println(BROWSER_SESSION_LOST_EMAIL);
            throw e;
        }
    }
    
    private void validateInvalidEmailResponse(String reply) {
        if (reply == null || reply.isEmpty()) {
            throw new AssertionError("No response received for invalid email test");
        }
        
        String lowerReply = reply.toLowerCase();
        boolean emailRejected = lowerReply.contains("invalid") || 
                                lowerReply.contains("incorrect") || 
                                lowerReply.contains("not valid") ||
                                lowerReply.contains("not be valid") ||
                                lowerReply.contains("not correct") ||
                                lowerReply.contains("not be correct") ||
                                lowerReply.contains("may not") ||
                                lowerReply.contains("appears") ||
                                lowerReply.contains("isn't valid") ||
                                lowerReply.contains("isn't correct") ||
                                lowerReply.contains("issue with") ||
                                lowerReply.contains("problem with") ||
                                lowerReply.contains("wrong") ||
                                lowerReply.contains("error") ||
                                lowerReply.contains("doesn't look") ||
                                lowerReply.contains("unable to verify") ||
                                lowerReply.contains("cannot verify") ||
                                lowerReply.contains("please verify") ||
                                lowerReply.contains("try again") ||
                                lowerReply.contains("please check") ||
                                lowerReply.contains("not recognized") ||
                                lowerReply.contains("not accepted") ||
                                lowerReply.contains("cannot accept");
        
        if (emailRejected) {
            System.out.println("✓ Invalid email correctly rejected by bot");
        } else {
            throw new AssertionError("FAIL: Bot accepted invalid email 'test@test.com' as valid! Response: " + reply);
        }
    }

    private static boolean isSchedulerReply(String text) {
        if (text == null) return false;
        String s = text.toLowerCase();
        return s.contains(KEYWORD_SCHEDULE) || s.contains(KEYWORD_EMAIL_ADDRESS) || s.contains(KEYWORD_NAME_AND_COMPANY);
    }

    private static void waitForMainChatInput(WebDriver driver, Duration timeout) {
        WebDriverWait w = new WebDriverWait(driver, timeout);
        w.ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    SearchContext r = getShadowRoot(driver);
                    List<WebElement> inputs = r.findElements(BY_CHAT_INPUT);
                    return !inputs.isEmpty() && inputs.get(0).isDisplayed();
                });
    }

    private static String waitAndGetNewAgentReply(WebDriver driver, int previousCount, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        WebElement bubble = wait.until(d -> {
            SearchContext r = getShadowRoot(driver);
            List<WebElement> msgs = r.findElements(BY_AGENT_TEXT);
            return msgs.size() > previousCount ? msgs.get(msgs.size() - 1) : null;
        });

        String text = "", curr;
        long stableFor = 0;
        while (stableFor < TEXT_STABLE_WAIT) {
            curr = bubble.getText().trim();
            if (!curr.equals(text)) {
                text = curr;
                stableFor = 0;
            } else {
                stableFor += TEXT_CHECK_INTERVAL;
            }
            safeSleep(TEXT_CHECK_INTERVAL);
        }
        return text;
    }

    public void handleCookies() {
        try {
            driver.findElement(By.xpath(COOKIE_DECLINE_XPATH)).click();
            System.out.println(COOKIES_DECLINED);
        } catch (NoSuchElementException ignored1) {
            try {
                driver.findElement(By.xpath(COOKIE_ALLOW_XPATH)).click();
                System.out.println(COOKIES_ACCEPTED);
            } catch (NoSuchElementException ignored2) {
                System.out.println(COOKIES_NONE);
            }
        }
    }

    public void clickChatBot(String url) {
        // Wait for page to fully load first
        WebDriverWait pageWait = new WebDriverWait(driver, Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        pageWait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
        
        // Wait for SDK shadow root to be available (allows time for auto-popup SDKs)
        safeSleep(AUTO_POPUP_WAIT);
        
        // Check if chat is already open (auto-popup enabled sites like edgematics)
        try {
            SearchContext shadowRoot = getShadowRoot();
            List<WebElement> chatInputs = shadowRoot.findElements(BY_CHAT_INPUT);
            if (!chatInputs.isEmpty() && chatInputs.get(0).isDisplayed()) {
                root = shadowRoot;
                System.out.println(CHAT_ALREADY_OPEN + url);
                return;
            }
        } catch (Exception ignored) {
            // Chat not auto-opened, proceed with clicking
        }
        
        // Try to click chat button (shorter timeout since auto-popup might have opened it)
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    SearchContext r = getShadowRoot(d);
                    WebElement btn = r.findElement(By.cssSelector(CHAT_BUTTON));
                    return btn.isDisplayed() ? btn : null;
                }).click();
            root = getShadowRoot();
            System.out.println(CHAT_OPENED + url);
            return;
        } catch (Exception e) {
            // Button click failed - check if chat is already open (late auto-popup)
            try {
                SearchContext shadowRoot = getShadowRoot();
                List<WebElement> chatInputs = shadowRoot.findElements(BY_CHAT_INPUT);
                if (!chatInputs.isEmpty() && chatInputs.get(0).isDisplayed()) {
                    root = shadowRoot;
                    System.out.println(CHAT_ALREADY_OPEN + url);
                    return;
                }
            } catch (Exception ignored) {}
            System.out.println(OPEN_FAILED + url + " | " + e.getMessage());
        }
    }

    public void expand() {
        clickShadowElementWithDelay(EXPAND_BUTTON, AFTER_EXPAND_DELAY, SDK_EXPANDED, EXPAND_FAILED);
    }

    public void close() {
        clickShadowElement(CLOSE_BUTTON, CLOSED, CLOSE_FAILED);
    }

    public int getDefaultSuggestionsCount() {
        try {
            root = Shadow.getRoot(driver);
            List<WebElement> items = Shadow.findAll(root, SUGGESTED_QUESTIONS_CONTAINER);
            System.out.println(SUGGESTIONS_COUNT_PREFIX + items.size());
            return items.size();
        } catch (Exception e) {
            System.out.println(SUGGESTIONS_COUNT_FAILED + e.getMessage());
            return 0;
        }
    }

    public void scheduleMeeting() {
        try {
            root = Shadow.getRoot(driver);
            Shadow.find(root, CALENDAR_BUTTON).click();
            schedulerClicked = true;
            Thread.sleep(AFTER_SCHEDULER_DELAY);
            System.out.println(SCHEDULER_CLICKED);
        } catch (NoSuchSessionException e) {
            System.err.println(BROWSER_SESSION_LOST_SCHEDULER);
            throw e;
        } catch (NoSuchElementException | StaleElementReferenceException | InterruptedException e) {
            System.out.println(SCHEDULER_CLICK_FAILED + e.getMessage());
        }
    }

    public String getLastReply() throws InterruptedException {
        try {
            List<WebElement> botReply = Waits.until(driver, d -> {
                SearchContext r = Shadow.getRoot(d);
                List<WebElement> list = Shadow.findAll(r, AGENT_TEXT);
                return list.isEmpty() ? null : list;
            }, AGENT_REPLY_TIMEOUT);

            safeSleep(AGENT_REPLY_DELAY);
            WebElement last = botReply.get(botReply.size() - 1);
            String text = last.getText().trim();
            if (!text.isEmpty()) System.out.println(AGENT_PREFIX + text);
            return text;
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println(NO_AGENT_REPLY);
            return EMPTY_STRING;
        } catch (org.openqa.selenium.NoSuchSessionException e) {
            System.err.println(BROWSER_SESSION_LOST_GENERIC + e.getMessage());
            return EMPTY_STRING;
        }
    }

    private static String doGreetingReply(WebDriver driver, String msg, boolean skipRetry) throws InterruptedException {
        waitForMainChatInput(driver, Duration.ofSeconds(CHAT_INPUT_TIMEOUT));

        String reply = EMPTY_STRING;
        for (int attempt = 0; attempt < MAX_GREETING_RETRIES; attempt++) {
            SearchContext root = getShadowRoot(driver);

            int before = root.findElements(BY_AGENT_TEXT).size();

            WebElement chatInput = root.findElement(BY_CHAT_INPUT);
            chatInput.click();
            chatInput.sendKeys(msg);

            root.findElement(BY_SEND).click();

            safeSleep(AFTER_SEND_DELAY);
            reply = waitAndGetNewAgentReply(driver, before, Duration.ofSeconds(SHADOW_ROOT_TIMEOUT));
            System.out.println(REPLY_PREFIX + reply);

            // Skip retry if scheduler was intentionally clicked
            if (!skipRetry && isSchedulerReply(reply) && attempt == 0) {
                System.out.println(SCHEDULER_RETRY);
                waitForMainChatInput(driver, Duration.ofSeconds(CHAT_INPUT_TIMEOUT));
                continue;
            }
            break;
        }

        SearchContext root = getShadowRoot(driver);
        int suggestions = root.findElements(BY_SUGGEST).size();
        if (suggestions == 0) {
            safeSleep(SUGGESTIONS_RECHECK_DELAY);
            root = getShadowRoot(driver);
            suggestions = root.findElements(BY_SUGGEST).size();
        }

        System.out.println(SUGGESTIONS_LABEL + suggestions);
        return reply;
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
            safeSleep(ELEMENT_CHECK_INTERVAL);
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
                    if (e.isDisplayed()) sb.append(e.getText().trim()).append(PIPE_SEPARATOR);
                } catch (Exception ignored) {}
            }
            return sb.toString();
        } catch (Exception e) {
            return EMPTY_STRING;
        }
    }

    private List<WebElement> waitForSuggestionsChange(String oldSig, long maxWaitMs) {
        long end = System.currentTimeMillis() + maxWaitMs;
        while (System.currentTimeMillis() < end) {
            String sig = suggestionSignature();
            if (!sig.isEmpty() && !sig.equals(oldSig)) {
                return waitForVisibleSuggestions(Duration.ofSeconds(SUGGESTIONS_CHANGE_TIMEOUT));
            }
            safeSleep(TEXT_CHECK_INTERVAL);
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
        String last = EMPTY_STRING;

        while (System.currentTimeMillis() - start < maxWaitMs) {
            try {
                SearchContext r = getShadowRoot();
                List<WebElement> bubbles = r.findElements(BY_AGENT_TEXT);

                if (bubbles.size() > oldCount) {
                    WebElement latest = bubbles.get(bubbles.size() - 1);
                    String cur = (String) ((JavascriptExecutor) driver)
                            .executeScript(JS_GET_TEXT, latest);
                    cur = (cur == null) ? EMPTY_STRING : cur.trim();

                    if (!cur.equals(last)) {
                        last = cur;
                        lastChange = System.currentTimeMillis();
                    } else if (System.currentTimeMillis() - lastChange >= quietMs) {
                        return last;
                    }
                }
            } catch (StaleElementReferenceException | NoSuchElementException ignored) {}
            safeSleep(ELEMENT_CHECK_INTERVAL);
        }
        return last;
    }

    private void easyClick(WebElement el) {
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript(JS_SCROLL_INTO_VIEW, el);
            ((JavascriptExecutor) driver).executeScript(JS_CLICK, el);
        }
    }

    // ===== REUSABLE UTILITY METHODS =====
    
    private static void safeSleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {}
    }
    
    private SearchContext getShadowRoot() {
        return driver.findElement(BY_HOST).getShadowRoot();
    }
    
    private static SearchContext getShadowRoot(WebDriver driver) {
        return driver.findElement(BY_HOST).getShadowRoot();
    }
    
    private void clickShadowElement(String selector, String successMsg, String failureMsg) {
        try {
            root = Shadow.getRoot(driver);
            Shadow.find(root, selector).click();
            System.out.println(successMsg);
        } catch (Exception e) {
            System.out.println(failureMsg + e.getMessage());
        }
    }
    
    private void clickShadowElementWithDelay(String selector, long delay, String successMsg, String failureMsg) {
        try {
            root = Shadow.getRoot(driver);
            Shadow.find(root, selector).click();
            safeSleep(delay);
            System.out.println(successMsg);
        } catch (Exception e) {
            System.out.println(failureMsg + e.getMessage());
        }
    }
    
    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(JS_SCROLL_INTO_VIEW, element);
        easyClick(element);
    }
    
    private static String safeText(WebElement el) {
        try { return el.getText().trim(); } catch (Exception e) { return EMPTY_STRING; }
    }

    public void clickRandomSuggestions() throws InterruptedException {
        List<WebElement> sugs = waitForVisibleSuggestions(Duration.ofSeconds(SUGGESTIONS_TIMEOUT));
        if (sugs.isEmpty()) {
            System.out.println(NO_SUGGESTIONS_VISIBLE);
            return;
        }
        System.out.println(SUGGESTIONS_FOUND + sugs.size() + SUGGESTIONS_SUFFIX);

        int clicks = Math.min(MAX_SUGGESTION_CLICKS, sugs.size());
        for (int i = 0; i < clicks; i++) {
            sugs = waitForVisibleSuggestions(Duration.ofSeconds(SUGGESTIONS_REFRESH_TIMEOUT));
            if (sugs.isEmpty() || i >= sugs.size()) {
                System.out.println(NO_SUGGESTIONS_NOW);
                break;
            }

            WebElement choice = sugs.get(i);
            String label = safeText(choice);
            System.out.println(CLICK_ARROW + label);

            int beforeReplies = getCurrentBotReplyCount();
            String oldSig = suggestionSignature();

            scrollAndClick(choice);

            String full = waitForNewBotReplyStable(beforeReplies, BOT_REPLY_QUIET_TIME, BOT_REPLY_MAX_WAIT);
            if (!full.isEmpty()) System.out.println(AGENT_PREFIX + full);

            List<WebElement> next = waitForSuggestionsChange(oldSig, SUGGESTIONS_CHANGE_WAIT);
            if (next.isEmpty()) {
                System.out.println(NO_NEXT_SUGGESTIONS);
                break;
            }

            safeSleep(CLICK_DELAY);
        }
    }
}
