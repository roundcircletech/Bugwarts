package testcases;

import files.ReusableMethods;

import static constants.Strings.*;
import static files.Payload.generateLoginPayload;

public class LoginTestcases {

    // Valid Login
    public static String login_001() {
        return generateLoginPayload(EXISTING_EMAIL, EXISTING_PASSWORD);
    }

    // Empty both -> {"message":"Invalid credentials"}
    public static String login_002() {
        return generateLoginPayload(EMPTY_STRING, EMPTY_STRING);
    }

    // Empty password {"message":"Invalid credentials"}
    public static String login_003() {return generateLoginPayload(EXISTING_EMAIL, EMPTY_STRING);}

    // Empty email {"message":"Invalid credentials"}
    public static String login_004() {return generateLoginPayload(EMPTY_STRING, EXISTING_PASSWORD);}

    // Invalid Email (checks like @-special symbol) {"message":"Invalid credentials"}
    public static String login_005() {return generateLoginPayload(INVALID_EMAIL, EXISTING_PASSWORD);}

    // Invalid password(Regex) {"message":"Invalid credentials"}
    public static String login_006() {
        return generateLoginPayload(EXISTING_EMAIL, WRONG_PASSWORD);
    }

    // user not signed up {"message":"User not found"}
    public static String login_007() { return generateLoginPayload(ReusableMethods.generateEmail(), EXISTING_PASSWORD);}

    // Invalid email - Invalid password {"message":"Invalid credentials"}
    public static String login_008() {
        return generateLoginPayload(INVALID_EMAIL, WRONG_PASSWORD);
    }

}
