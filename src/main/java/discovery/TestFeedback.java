package discovery;

import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testcases.FeedbackTestCases;

import static constants.DataProviders.FEEDBACK_DATA;
import static constants.Headers.X_AUTH;
import static constants.Strings.*;
import static constants.Urls.*;
import static io.restassured.RestAssured.given;

public class TestFeedback {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @DataProvider(name = FEEDBACK_DATA)
    public Object[][] feedbackData() {
        return new Object[][]{
                {FeedbackTestCases.feedback_001(), 200},  // Valid feedback
                {FeedbackTestCases.feedback_002(), 400},  // Missing conversation ID
                {FeedbackTestCases.feedback_003(), 400},  // Invalid status value
                {FeedbackTestCases.feedback_004(), 200},  // Empty comment allowed
                {FeedbackTestCases.feedback_005(), 400},  // Null status
                {FeedbackTestCases.feedback_007(), 400},  // Empty status
                {FeedbackTestCases.feedback_008(), 400},  // Invalid conversation ID format
                {FeedbackTestCases.feedback_009(), 404},  // Non-existent conversation ID
                {FeedbackTestCases.feedback_010(), 400},  // Non-allowed status (e.g., NEUTRAL)
                {FeedbackTestCases.feedback_011(), 400}   // Mix of invalid values
        };
    }


    @Test(dataProvider = FEEDBACK_DATA)
    public void sendFeedbackTest(String payload, int expectedStatusCode) {

        String conversationId = ReusableMethods.extractKeyFromPayload(payload, CONVERSATION_ID);
        String feedbackUrl = String.format(FEEDBACK_URL, conversationId);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header(X_AUTH, AUTH_TOKEN)
                .body(payload)
                .when()
                .post(feedbackUrl)
                .then()
                .log().all()
                .assertThat()
                .statusCode(expectedStatusCode);
    }

}
