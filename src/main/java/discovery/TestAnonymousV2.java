package discovery;

import io.restassured.RestAssured;
import org.testng.annotations.*;
import testcases.AnonymousV2Testcases;

import static constants.Strings.*;
import static constants.Urls.*;
import static io.restassured.RestAssured.given;


public class TestAnonymousV2 {

    @BeforeClass
    private void setup() {RestAssured.baseURI = BASE_URL;}

    @DataProvider(name = ANONYMOUS_V2_PAYLOAD)
    public Object[][] anonymousData() {
        return new Object[][] {
                {AnonymousV2Testcases.anonymousV2_001(), 200},
                {AnonymousV2Testcases.anonymousV2_002(), 400},
        };
    }

    @Test(dataProvider = ANONYMOUS_V2_PAYLOAD)
    private void testAnonymousAuth(String payloadData, int expectedStatusCode) {
        given()
                .header(ACCEPT, ANONYMOUS_V2_ACCEPT)
                .header(CONTENT_TYPE,   CONTENT_TYPE_JSON)
                .body(payloadData)
                .when().post(ANONYMOUS_V2)
                .then().log().all().assertThat().statusCode(expectedStatusCode).extract().response();
    }
}
