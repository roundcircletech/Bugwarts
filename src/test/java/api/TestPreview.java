package api;

import core.CsvUtils;
import io.restassured.RestAssured;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static constants.DataProviders.PREVIEW_PAYLOAD;
import static constants.Headers.*;
import static constants.Urls.BIFROST_URL;
import static io.restassured.RestAssured.given;

public class TestPreview {
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BIFROST_URL;
    }

    @DataProvider(name = PREVIEW_PAYLOAD)
    public Object[][] previewData() throws Exception {
        return CsvUtils.toDataProvider(PreviewTestcases.getAllPreviewPayloads());
    }

    @Test(dataProvider = PREVIEW_PAYLOAD)
    public void testPreview(String payloadData) {
        JSONObject payload = new JSONObject(payloadData);
        given()
                .log().all()
                .header(ACCEPT, APPLICATION_JSON)
                .header(CLIENT_ID, payload.getString(CLIENT_ID))
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(USER_AGENT, AGENT_MOZILLA)
                .header(ORIGIN, payload.getString(ORIGIN))
                .header(REFERER, payload.getString(REFERER))
        .when()
                .get(BIFROST_URL)
        .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }
}