package files;

import java.util.UUID;

public class Payload {

    public static UUID sessionId() {
        return UUID.randomUUID();
    }

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
                + "    \"sessionId\" : \"" + sessionId() + "\"\n"
                + "}";
    }



}
