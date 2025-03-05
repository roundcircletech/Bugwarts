package files;

import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static constants.ErrorStrings.INVITE_USER_PAYLOAD_ERROR;
import static constants.Strings.*;

public class Payload {

    public static String generateSignUpPayload(String email, String password, String name) {
        return "{\n"
                + "    \"email\" : \"" + email + "\",\n"
                + "    \"password\" : \"" + password + "\",\n"
                + "    \"name\" : \"" + name + "\"\n"
                + "}";
    }

    public static String generateLoginPayload(String email, String password) {
        return "{\n"
                + "    \"email\" : \"" + email + "\",\n"
                + "    \"password\" : \"" + password + "\",\n"
                + "    \"sessionId\" : \"" + ReusableMethods.sessionId() + "\"\n"
                + "}";
    }

    public static String generateAnonymousV2Payload(UUID sessionId){
        if(sessionId != null) {
            JSONObject requestBody = new JSONObject();
            requestBody.put(SESSION_ID, sessionId.toString());
            return requestBody.toString();
        }
        else return EMPTY_STRING;
    }

    public static String generateInviteUserPayload(String recipient, String inviteType, String action) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put(RECIPIENT, List.of(recipient));
            payload.put(INVITE_TYPE, inviteType);
            payload.put(ACTION, action);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(payload);  // Convert map to JSON string
        } catch (Exception e) {
            throw new RuntimeException(INVITE_USER_PAYLOAD_ERROR, e);
        }
    }
}
