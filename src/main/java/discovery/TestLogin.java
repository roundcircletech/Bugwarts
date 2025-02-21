package discovery;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testcases.LoginTestcases;

import static constants.Strings.*;
import static constants.Urls.*;
import static io.restassured.RestAssured.*;

public class TestLogin {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @DataProvider(name = LOGIN_PAYLOAD)
    public Object[][] loginData() {
        return new Object[][] {
                {LoginTestcases.login_001(), 200}, // Valid login, 200
                {LoginTestcases.login_002(), 400}, // Both Empty, 400
                {LoginTestcases.login_003(), 400}, // Empty password, 400
                {LoginTestcases.login_004(), 400},  // empty email, 400
                {LoginTestcases.login_005(), 400}, // Invalid email, 400
                {LoginTestcases.login_006(), 400}, // Invalid password (regex), 400
                {LoginTestcases.login_007(), 400}, // user not signup, 400
                {LoginTestcases.login_008(), 400}  // Invalid both, 400
        };
    }

    @Test(dataProvider = LOGIN_PAYLOAD)
    public void loginTest(String payloadData, int expectedStatusCode) {
        given().header(CONTENT_TYPE, CONTENT_TYPE_JSON)
                .body(payloadData)
                .when().post(LOGIN_URL)
                .then().log().all().assertThat().statusCode(expectedStatusCode);
    }
}
