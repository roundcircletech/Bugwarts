package files;

import io.restassured.path.json.JsonPath;
import java.util.UUID;
import static constants.Strings.*;

public class ReusableMethods {

    public static JsonPath rawToJson( String RawData) {
        return new JsonPath(RawData);
    }

    public static String generateEmail() {
        return TEST_NAME + UUID.randomUUID().toString().substring(0, 8) + TEST_DOMAIN;
    }

    public static UUID sessionId() {
        return UUID.randomUUID();
    }

}