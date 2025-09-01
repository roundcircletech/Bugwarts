package actions;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;

public class reusableMethods {
    public static void clickElement(SearchContext shadowRoot, String selector, String successMsg) {
        try {
            shadowRoot.findElement(By.cssSelector(selector)).click();
            Thread.sleep(3000);  // delay if needed
            System.out.println(successMsg);
        } catch (Exception e) {
            System.out.println("Couldn't perform action: " + e.getMessage());
        }
    }
}
