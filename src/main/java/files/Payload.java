package files;

import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

import static constants.ErrorStrings.*;
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

    public static String generateFeedbackPayload(String conversationId, String status, String comment) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put(CONVERSATION_ID, conversationId);
            payload.put(STATUS, status);
            payload.put(COMMENT, comment);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(payload);  // Convert map to JSON string
        } catch (Exception e) {
            throw new RuntimeException(FEEDBACK_PAYLOAD_ERROR, e);
        }
    }

    public static String generateBookDemoPayload(String conversationId, String toolName, String source,
                                                 String name, String email, String startTime,
                                                 String endTime, String phone) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("conversationId", conversationId);
            payload.put("toolName", toolName);
            payload.put("source", source);
            payload.put("name", name);
            payload.put("email", email);
            payload.put("startTime", startTime);
            payload.put("endTime", endTime);
            payload.put("phone", phone);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(payload);

        } catch (Exception e) {
            throw new RuntimeException("Error generating Book Demo payload", e);
        }
    }
}
