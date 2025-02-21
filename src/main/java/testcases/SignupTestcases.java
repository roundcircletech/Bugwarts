package testcases;

import static constants.Strings.*;
import static files.ReusableMethods.generateEmail;
import static files.Payload.generateSignUpPayload;

public class SignupTestcases {

    // valid email, fullName, password
    public static String signup_001() {return generateSignUpPayload(generateEmail(),TEST_PASSWORD ,TEST_NAME);}

    // signup with an existing emails
    public static String signup_002() {
        return generateSignUpPayload(EXISTING_EMAIL, TEST_PASSWORD, TEST_NAME);
    }

    // signup with invalid email format
    public static String signup_003() {
        return generateSignUpPayload(INVALID_EMAIL, TEST_PASSWORD, TEST_NAME);
    }

    // signup with weak password regex
    public static String signup_004() {
        return generateSignUpPayload(generateEmail(), WEAK_PASSWORD, TEST_NAME);
    }

    // signup with all empty
    public static String signup_005() {
        return generateSignUpPayload(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
    }

    // signup with empty email
    public static String signup_006() {
        return generateSignUpPayload(EMPTY_STRING, TEST_PASSWORD, TEST_NAME);
    }

    // signup with empty password
    public static String signup_007() {return generateSignUpPayload(generateEmail(),EMPTY_STRING ,TEST_NAME);}

    // signup with empty name
    public static String signup_008() {return generateSignUpPayload(generateEmail(),TEST_PASSWORD ,EMPTY_STRING);}

    // full name field least contains 3 characters
    public static String signup_009() {return generateSignUpPayload(generateEmail(),TEST_PASSWORD ,TWO_CHARACTERS);}

    // full name field should not contain any special characters
    public static String signup_010() {return generateSignUpPayload(generateEmail(),TEST_PASSWORD ,CONTAINS_SPECIAL_CHARACTERS);}
}
