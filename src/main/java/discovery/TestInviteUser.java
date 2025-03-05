package discovery;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testcases.InviteUserTestCases;

import static constants.DataProviders.INVITE_DATA;
import static constants.Headers.X_AUTH;
import static constants.Strings.AUTH_TOKEN;
import static constants.Urls.*;
import static io.restassured.RestAssured.given;

public class TestInviteUser {


    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @DataProvider(name = INVITE_DATA)
    public Object[][] inviteData() {
        return new Object[][] {
                {(InviteUserTestCases.inviteUser_001()), 200}, // Valid invite
                {(InviteUserTestCases.inviteUser_002()), 400}, // Empty recipient list
                {(InviteUserTestCases.inviteUser_003()), 400}, // Invalid email format
                {(InviteUserTestCases.inviteUser_004()), 400}, // Invalid invite type
                {(InviteUserTestCases.inviteUser_005()), 400}  // Invalid action
        };
    }

    @Test(dataProvider =INVITE_DATA)
    public void sendInviteTest(String payload, int expectedStatusCode) {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header(X_AUTH, AUTH_TOKEN)
                .body(payload)
                .when()
                .post(INVITE_USER_URL)
                .then()
                .log().all()
                .assertThat()
                .statusCode(expectedStatusCode);
    }
}
