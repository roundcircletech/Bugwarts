package discovery;

import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testcases.BookDemoTestCases;

import static constants.DataProviders.BOOK_DEMO_DATA;
import static constants.Headers.X_AUTH;
import static constants.Strings.*;
import static constants.Urls.*;
import static io.restassured.RestAssured.given;

public class TestBookDemo {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @DataProvider(name = BOOK_DEMO_DATA)
    public Object[][] bookDemoData() {
        return new Object[][]{
                {BookDemoTestCases.bookDemo_001(), 200, "Valid demo booking"},
                {BookDemoTestCases.bookDemo_002(), 400, "Missing toolName"},
                {BookDemoTestCases.bookDemo_003(), 400, "Invalid email format"},
                {BookDemoTestCases.bookDemo_004(), 400, "Empty name field"},
                {BookDemoTestCases.bookDemo_005(), 401, "Unauthorized request"},
                {BookDemoTestCases.bookDemo_006(), 400, "Null source"},
                {BookDemoTestCases.bookDemo_007(), 400, "Invalid phone format"},
                {BookDemoTestCases.bookDemo_008(), 400, "Empty startTime"},
                {BookDemoTestCases.bookDemo_009(), 400, "Start time after end time"},
                {BookDemoTestCases.bookDemo_010(), 400, "End time missing"},
                {BookDemoTestCases.bookDemo_011(), 400, "Special characters in name"},
                {BookDemoTestCases.bookDemo_012(), 400, "Invalid source type"},
                {BookDemoTestCases.bookDemo_013(), 400, "Long comment in name field"},
                {BookDemoTestCases.bookDemo_014(), 400, "Invalid time format"},
                {BookDemoTestCases.bookDemo_015(), 400, "Mix of invalid values"}
        };
    }

    @Test(dataProvider = BOOK_DEMO_DATA)
    public void bookDemoTest(String payload, int expectedStatusCode, String testCaseDescription) {

        String conversationId = ReusableMethods.extractKeyFromPayload(payload, CONVERSATION_ID);
        String bookDemoUrl = String.format(BOOK_DEMO_URL, conversationId);

        System.out.println("Running Test Case: " + testCaseDescription);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header(X_AUTH, AUTH_TOKEN)
                .body(payload)
                .when()
                .post(bookDemoUrl)
                .then()
                .log().all()
                .assertThat()
                .statusCode(expectedStatusCode);
    }
}
