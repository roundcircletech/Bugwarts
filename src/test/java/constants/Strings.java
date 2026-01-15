package constants;

public class Strings {
    // Empty
    public static final String EMPTY_STRING = "";
    
    // Browsers
    public static final String CHROME = "chrome";
    public static final String FIREFOX = "firefox";
    public static final String BROWSER_PROPERTY = "browser";
    
    // Test data
    public static final String GREETING_MSG = "Hi Agent, I am Avishi!";
    public static final String INVALID_EMAIL = "testinvalid1234@test.com";
    
    // Scheduler keywords
    public static final String KEYWORD_SCHEDULE = "schedule";
    public static final String KEYWORD_EMAIL_ADDRESS = "email address";
    public static final String KEYWORD_NAME_AND_COMPANY = "name and company";
    
    // Separator
    public static final String PIPE_SEPARATOR = "|";
    
    // Browser options
    public static final String CHROME_OPTION_HEADLESS = "--headless=new";
    public static final String CHROME_OPTION_WINDOW_SIZE = "--window-size=1920,1080";
    public static final String CHROME_OPTION_NO_SANDBOX = "--no-sandbox";
    public static final String CHROME_OPTION_DISABLE_SHM = "--disable-dev-shm-usage";
    // Realistic Windows user agent (more common, harder to detect as bot)
    public static final String CHROME_USER_AGENT = "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    // Anti-detection options
    public static final String CHROME_DISABLE_AUTOMATION = "--disable-blink-features=AutomationControlled";
    public static final String CHROME_DISABLE_GPU = "--disable-gpu";
    public static final String CHROME_REMOTE_DEBUGGING = "--remote-debugging-port=9222";
    
    public static final String FIREFOX_OPTION_HEADLESS = "--headless";
    public static final String FIREFOX_OPTION_WIDTH = "--width=1920";
    public static final String FIREFOX_OPTION_HEIGHT = "--height=1080";
    
    // JavaScript scroll position
    public static final String JS_SCROLL_BLOCK_CENTER = "center";
}

