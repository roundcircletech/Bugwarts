package constants;

public class Selectors {
    // Shadow DOM
    public static final String HOST_COMPONENT = "my-component";
    
    // Chat elements
    public static final String CHAT_BUTTON = "button.ChatButton-module_sdkChatButton__M1mKI";
    public static final String CHAT_INPUT = "textarea[role='textbox'][aria-label='Chat input']";
    public static final String SEND_BUTTON = "button.ChatInputBox-module_sdkSendButton__CLWm6";
    public static final String CALENDAR_BUTTON = "button.ChatInputBox-module_sdkCalendarButton__r3Rt3";
    
    // Header buttons
    public static final String EXPAND_BUTTON = "button.ChatHeader-module_sdkExpandButton__qtONk";
    public static final String CLOSE_BUTTON = "button.ChatHeader-module_sdkCloseButton__AQvQv";
    
    // Content
    public static final String AGENT_TEXT = "div[class*='AiText-module_textContainer']";
    public static final String SUGGESTIONS = "h1[class*='suggestiveResponse']";
    public static final String SUGGESTED_QUESTIONS_CONTAINER = "div[class*='sdkSuggestedQuestionsContainer'] button";
    
    // Cookies (XPath)
    public static final String COOKIE_DECLINE_XPATH = "//div[contains(text(),'Decline')]";
    public static final String COOKIE_ALLOW_XPATH = "//div[contains(text(),'Allow Cookies')]";
    
    // JavaScript scroll
    public static final String JS_SCROLL_INTO_VIEW = "arguments[0].scrollIntoView({block:'center'});";
    public static final String JS_CLICK = "arguments[0].click();";
    public static final String JS_GET_TEXT = "return arguments[0].innerText || arguments[0].textContent || '';";
}

