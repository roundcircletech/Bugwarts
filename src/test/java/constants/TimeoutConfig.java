package constants;

public class TimeoutConfig {
    // Wait timeouts (in seconds)
    public static final int DEFAULT_TIMEOUT = 10;
    public static final int SHADOW_ROOT_TIMEOUT = 30;
    public static final int AGENT_REPLY_TIMEOUT = 20;
    public static final int CHAT_INPUT_TIMEOUT = 20;
    public static final int SUGGESTIONS_TIMEOUT = 45;
    public static final int SUGGESTIONS_REFRESH_TIMEOUT = 30;
    public static final int SUGGESTIONS_CHANGE_TIMEOUT = 5;
    
    // Wait timeouts (in milliseconds)
    public static final long TEXT_STABLE_WAIT = 600L;
    public static final long TEXT_CHECK_INTERVAL = 150L;
    public static final long ELEMENT_CHECK_INTERVAL = 120L;
    public static final long SUGGESTIONS_CHANGE_WAIT = 20000L;
    public static final long BOT_REPLY_QUIET_TIME = 900L;
    public static final long BOT_REPLY_MAX_WAIT = 25000L;
    public static final long CLICK_DELAY = 500L;
    
    // Sleep durations (in milliseconds)
    public static final long AFTER_EXPAND_DELAY = 400L;
    public static final long AFTER_SCHEDULER_DELAY = 2000L;
    public static final long AFTER_SEND_DELAY = 3000L;
    public static final long AGENT_REPLY_DELAY = 9000L;
    public static final long SUGGESTIONS_RECHECK_DELAY = 5000L;
    public static final long AUTO_POPUP_WAIT = 5000L;
    public static final long RETRY_DELAY = 2000L;
    
    // Page load timeout (in seconds)
    public static final int PAGE_LOAD_TIMEOUT = 30;
    
    // Retry configuration
    public static final int MAX_GREETING_RETRIES = 2;
    public static final int MAX_SUGGESTION_CLICKS = 1;
}

