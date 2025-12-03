package constants;

public class Messages {
    // Cookie handling
    public static final String COOKIES_DECLINED = "Cookies: declined";
    public static final String COOKIES_ACCEPTED = "Cookies: accepted";
    public static final String COOKIES_NONE = "Cookies: none";
    
    // Chat operations
    public static final String CHAT_OPENED = "Chat opened: ";
    public static final String OPEN_FAILED = "Open failed: ";
    public static final String SDK_EXPANDED = "SDK Successfully Expanded";
    public static final String EXPAND_FAILED = "Expand failed: ";
    public static final String CLOSED = "Closed";
    public static final String CLOSE_FAILED = "Close failed: ";
    
    // Suggestions
    public static final String SUGGESTIONS_COUNT_PREFIX = "No. of Default Suggestive Responses: ";
    public static final String SUGGESTIONS_COUNT_FAILED = "Suggestions count failed: ";
    public static final String SUGGESTIONS_FOUND = "Found ";
    public static final String SUGGESTIONS_SUFFIX = " suggestive responses";
    public static final String NO_SUGGESTIONS_VISIBLE = "No suggestions visible initially, skipping.";
    public static final String NO_SUGGESTIONS_NOW = "No suggestions to click now, stopping.";
    public static final String NO_NEXT_SUGGESTIONS = "No next suggestions yet (stopping to avoid timeout).";
    
    // Scheduler
    public static final String SCHEDULER_CLICKED = "Scheduler clicked";
    public static final String SCHEDULER_CLICK_FAILED = "Scheduler click failed: ";
    public static final String SCHEDULER_RETRY = "Scheduler: retry";
    
    // Agent replies
    public static final String AGENT_PREFIX = "Agent: ";
    public static final String REPLY_PREFIX = "Reply: ";
    public static final String NO_AGENT_REPLY = "No agent reply within wait window (continuing)";
    public static final String CLICK_ARROW = "Click -> ";
    
    // Suggestions count
    public static final String SUGGESTIONS_LABEL = "Suggestions: ";
    
    // Error messages
    public static final String BROWSER_SESSION_LOST_GREETING = "Browser session lost during greeting reply";
    public static final String BROWSER_SESSION_LOST_EMAIL = "Browser session lost during email test";
    public static final String BROWSER_SESSION_LOST_SCHEDULER = "Browser session lost during scheduler click";
    public static final String BROWSER_SESSION_LOST_GENERIC = "Browser session lost: ";
    public static final String TEST_FAILED_FOR = "Test failed for ";
    public static final String DRIVER_INIT_FAILED = "Failed to initialize driver: ";
    public static final String TEARDOWN_ERROR = "Error during tearDown: ";
}

