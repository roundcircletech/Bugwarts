package constants;

public class Selectors {
    // Shadow DOM
    public static final String HOST_COMPONENT = "my-component";
    
    // Chat elements — use partial class match; CSS module hashes change on SDK rebuild
    public static final String CHAT_BUTTON = "button[class*='sdkChatButton']";
    public static final String CHAT_INPUT = "textarea[role='textbox'][aria-label='Chat input']";
    public static final String SEND_BUTTON = "button[class*='sdkSendButton']";
    public static final String CALENDAR_BUTTON = "button[class*='sdkCalendarButton']";
    
    // Header buttons
    public static final String EXPAND_BUTTON = "button[class*='sdkExpandButton']";
    public static final String CLOSE_BUTTON = "button[class*='sdkCloseButton']";
    
    // Content
    public static final String AGENT_TEXT = "div[class*='AiText-module_textContainer']";
    public static final String SUGGESTIONS = "h1[class*='suggestiveResponse']";
    public static final String SUGGESTED_QUESTIONS_CONTAINER = "div[class*='sdkSuggestedQuestionsContainer'] button";
    
    // Cookies (XPath)
    public static final String COOKIE_DECLINE_XPATH = "//div[contains(text(),'Decline')]";
    public static final String COOKIE_ALLOW_XPATH = "//div[contains(text(),'Allow Cookies')]";
    
    // Increff Design 2 (floating bar) — used only for increff.com
    public static final String INCREFF_FLOATING_BAR_INPUT = "input[aria-label='Open chat to type']";
    public static final String INCREFF_FLOATING_BAR_OPEN = "div[role='button'][aria-label='Open chat to type']";
    public static final String INCREFF_CHAT_INPUT = "textarea[aria-label='Chat input']";
    public static final String INCREFF_SEND_BUTTON = "button[class*='sdkSendButton']";
    public static final String INCREFF_CALENDAR_BUTTON = "button[class*='sdkCalendarButton']";
    public static final String INCREFF_EXPAND_BUTTON = "button[class*='sdkExpandButton']";
    public static final String INCREFF_CLOSE_BUTTON =
            "button[class*='closeBtn'], button[class*='sdkCloseButton']";
    public static final String INCREFF_SUGGESTIONS = "h1[class*='suggestiveResponse'], div[class*='suggestiveResponse']";
    public static final String INCREFF_SUGGESTED_QUESTIONS_CONTAINER =
            "div[class*='sdkSuggestedQuestionsContainer'] button";
    public static final String INCREFF_COOKIE_REJECT_XPATH = "//button[contains(.,'Reject All')]";
    public static final String INCREFF_COOKIE_ACCEPT_XPATH = "//button[contains(.,'Accept All')]";
    
    // JavaScript scroll
    public static final String JS_SCROLL_INTO_VIEW = "arguments[0].scrollIntoView({block:'center'});";
    public static final String JS_CLICK = "arguments[0].click();";
    public static final String JS_GET_TEXT = "return arguments[0].innerText || arguments[0].textContent || '';";
}
