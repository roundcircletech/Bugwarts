package testcases;

import static constants.Strings.*;
import static files.Payload.generateInviteUserPayload;

public class InviteUserTestCases {

    // Valid invite
    public static String inviteUser_001() {
        return generateInviteUserPayload(EXISTING_EMAIL, "USER", "ACCEPT");
    }

    // Empty recipient list
    public static String inviteUser_002() {
        return generateInviteUserPayload(EMPTY_STRING, "USER", "ACCEPT");
    }

    // Invalid email format
    public static String inviteUser_003() {
        return generateInviteUserPayload(INVALID_EMAIL, "USER", "ACCEPT");
    }

    // Invalid invite type
    public static String inviteUser_004() {
        return generateInviteUserPayload(EXISTING_EMAIL, "ADMIN", "ACCEPT");
    }

    // Invalid action
    public static String inviteUser_005() {
        return generateInviteUserPayload(EXISTING_EMAIL, "USER", "DECLINE");
    }


}
