package discovery;

import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static constants.Strings.*;
import static constants.Urls.*;
import static io.restassured.RestAssured.given;

public class TestSignup {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @DataProvider(name = SIGNUP_PAYLOAD)
    public Object[][] data() {
        return new Object[][]{
                {testcases.SignupTestcases.signup_001(), 200, "Please verify user - Email Sent to registered email"}, // valid email, fullName, password
                {testcases.SignupTestcases.signup_002(), 400, "User already signed up"}, // signup with an existing emails
                {testcases.SignupTestcases.signup_003(), 400, "OOPS SOMETHING WENT WRONG"}, // signup with invalid email format
                {testcases.SignupTestcases.signup_004(), 400, "Invalid password format"}, // signup with weak password regex
                {testcases.SignupTestcases.signup_005(), 400, "Invalid password format"}, // signup with all empty
                {testcases.SignupTestcases.signup_006(), 400, "Oops Something went Wrong!"},//signup with empty email
                {testcases.SignupTestcases.signup_007(), 400, "Invalid password format"}, // signup with empty password
                {testcases.SignupTestcases.signup_008(), 400, "Oops Something went Wrong!"}, // signup with empty name
                {testcases.SignupTestcases.signup_009(), 400, "Oops Something went Wrong!"}, // full name field least contains 3 characters
                {testcases.SignupTestcases.signup_010(), 400, "Oops Something went Wrong!"}, // full name field should not contain any special characters
        };
    }

    @Test(dataProvider = SIGNUP_PAYLOAD)
    public void signupTest(String payload, int statusCode, String expectedMessage) {
        String responseBody = given().log().all().header(CONTENT_TYPE, CONTENT_TYPE_JSON)
                .body(payload)
                .when().post(SIGNUP_URL)
                .then().log().all().assertThat().statusCode(statusCode)
                .contentType(CONTENT_TYPE_TEXT).extract().asString();

        Assert.assertEquals(responseBody, expectedMessage);
    }

}

