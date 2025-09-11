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

    public int greetingReply() throws InterruptedException {
        return doGreetingReply(this.driver);
    }

    private static boolean isSchedulerReply(String text) {
        if (text == null) return false;
        String s = text.toLowerCase();
        return s.contains("schedule") || s.contains("email address") || s.contains("name and company");
    }

    private static void waitForMainChatInput(WebDriver driver, Duration timeout) {
        new WebDriverWait(driver, timeout).until(d -> {
            SearchContext r = d.findElement(By.cssSelector("my-component")).getShadowRoot();
            return !r.findElements(By.cssSelector("textarea[role='textbox'][aria-label='Chat input']")).isEmpty();
        });
    }

    private static String waitAndGetNewAgentReply(WebDriver driver, int previousCount, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        WebElement bubble = wait.until(d -> {
            SearchContext r = d.findElement(By.cssSelector("my-component")).getShadowRoot();
            List<WebElement> msgs = r.findElements(By.cssSelector("div[class*='AiText-module_textContainer']"));
            return msgs.size() > previousCount ? msgs.get(msgs.size() - 1) : null;
        });

        String text = "", curr;
        long stableFor = 0;
        while (stableFor < 600) { // ~0.6s stable text
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
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return "";
        }
    }

    private static int doGreetingReply(WebDriver driver) throws InterruptedException {
        waitForMainChatInput(driver, Duration.ofSeconds(10));

        String reply;
        for (int attempt = 0; attempt < 2; attempt++) {
            SearchContext root = driver.findElement(By.cssSelector("my-component")).getShadowRoot();

            int before = root.findElements(By.cssSelector("div[class*='AiText-module_textContainer']")).size();

            WebElement chatInput = root.findElement(By.cssSelector("textarea[role='textbox'][aria-label='Chat input']"));
            chatInput.click();
            chatInput.sendKeys("Hi");

            root.findElement(By.cssSelector("button.ChatInputBox-module_sdkSendButton__CLWm6")).click();

            Thread.sleep(3000);
            reply = waitAndGetNewAgentReply(driver, before, Duration.ofSeconds(12));
            System.out.println("Reply: " + reply);

            if (isSchedulerReply(reply) && attempt == 0) {
                System.out.println("Scheduler -> retry");
                waitForMainChatInput(driver, Duration.ofSeconds(10));
                continue;
            }
            break;
        }

        SearchContext root = driver.findElement(By.cssSelector("my-component")).getShadowRoot();
        int suggestions = root.findElements(By.cssSelector("h1[class*='suggestiveResponse']")).size();
        if (suggestions == 0) {
            Thread.sleep(600);
            root = driver.findElement(By.cssSelector("my-component")).getShadowRoot();
            suggestions = root.findElements(By.cssSelector("h1[class*='suggestiveResponse']")).size();
        }

        System.out.println("Suggestions: " + suggestions);
        return suggestions;
    }

    public void clickRandomSuggestions() throws InterruptedException {
        String sug = "h1[class*='suggestiveResponse']";
        String reply = "div[class*='AiText-module_textContainer']";

        for (int i = 0; i < 3; i++) {
            root = Shadow.getRoot(driver);
            List<WebElement> suggestions = Shadow.findAll(root, sug);
            if (i >= suggestions.size()) break;

            WebElement choice = suggestions.get(i);
            String label = "";
            try {
                label = choice.getText().trim();
            } catch (StaleElementReferenceException e) {
                root = Shadow.getRoot(driver);
                suggestions = Shadow.findAll(root, sug);
                if (i < suggestions.size()) {
                    choice = suggestions.get(i);
                    label = choice.getText().trim();
                }
            }

            System.out.println("Click -> " + label);

            int before = Shadow.findAll(root, reply).size();

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                    choice
            );


            WebElement finalChoice = choice;
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(d ->
                    (Boolean) ((JavascriptExecutor) d).executeScript(
                            "const el = arguments[0];" +
                                    "if (!el) return false;" +
                                    "const r = el.getBoundingClientRect();" +
                                    "const x = Math.floor(r.left + r.width/2);" +
                                    "const y = Math.floor(r.top + r.height/2);" +
                                    "const root = el.getRootNode();" +
                                    "const topEl = (root && root.elementFromPoint) ? root.elementFromPoint(x, y) : document.elementFromPoint(x, y);" +
                                    "return topEl === el || el.contains(topEl);",
                            finalChoice
                    )
            );

            try {
                choice.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", choice);
            } catch (StaleElementReferenceException e) {
                root = Shadow.getRoot(driver);
                suggestions = Shadow.findAll(root, sug);
                if (i < suggestions.size()) {
                    suggestions.get(i).click();
                }
            }
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> Shadow.findAll(Shadow.getRoot(d), reply).size() > before);

            Thread.sleep(5000);
        }
    }
}