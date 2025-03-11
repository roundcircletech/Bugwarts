package files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import constants.ErrorStrings;
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

    public static String extractKeyFromPayload(String payload, String key) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payload);

            if (jsonNode.has(key) && !jsonNode.get(key).isNull()) {
                return jsonNode.get(key).asText();
            } else {
                throw new IllegalArgumentException(String.format(ErrorStrings.MISSING_OR_NULL_KEY, key));
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format(ErrorStrings.ERROR_EXTRACTING_KEY, key), e);
        }
    }



}