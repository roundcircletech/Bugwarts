package testScripts;

import Utils.CookiesUtils;
import Utils.ExcelUtils;
import actions.chatActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.time.Duration;

import static constants.CssSelectors.*;
import static constants.DataProviders.CHATBOT_URLS;
import static constants.Urls.CHATBOT_URLS_EXCEL;

public class ChatBotExcelTest {

	WebDriver driver;

	@BeforeMethod
	public void setUp() {
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
	//URLs from Excel to tests
	@DataProvider(name = CHATBOT_URLS)
	public Object[][] provideUrls() throws IOException {
		return ExcelUtils.getExcelData(CHATBOT_URLS_EXCEL);
	}

	@Test(dataProvider = CHATBOT_URLS, priority = 2)
	public void testSites(String urls) throws InterruptedException {
		driver.get(urls);

		String result = CookiesUtils.handleCookiesIfPresent(driver, REJECT_COOKIES,ACCEPT_COOKIES);
		System.out.println(result);
		WebElement shadowHost = driver.findElement(By.cssSelector(MY_COMPONENT));
		shadowHost.getShadowRoot();
		chatActions.clickChatBot(urls,shadowHost.getShadowRoot(),SDK_CHAT); //opening sdk
		chatActions.expandChatBot(shadowHost.getShadowRoot(), SDK_EXPAND_BUTTON); //for expanding sdk
		int suggestions = chatActions.getDefaultSuggestionsCount(shadowHost.getShadowRoot());
		Assert.assertEquals(suggestions, 3, "Suggestions mismatch for " + urls);
		chatActions.scheduleMeeting(shadowHost.getShadowRoot(), SCHEDULE_BUTTON); // scheduling a meeting
		chatActions.getLastReply(driver, shadowHost.getShadowRoot());
		chatActions.greetingReply(shadowHost.getShadowRoot());
		chatActions.closeChatBot(shadowHost.getShadowRoot(), SDK_CLOSE_BUTTON); //closing sdk
	}
}