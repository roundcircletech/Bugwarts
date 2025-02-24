package files;

import org.json.JSONObject;
import java.util.UUID;

import static constants.Strings.EMPTY_STRING;
import static constants.Strings.SESSION_ID;

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



}
