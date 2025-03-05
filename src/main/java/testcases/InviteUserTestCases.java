package testcases;

import static files.Payload.generateInviteUserPayload;

public class InviteUserTestCases {

    // Valid invite
    public static String inviteUser_001() {
        return generateInviteUserPayload("[\"avishi.sharma@roundcircle.tech\"]", "USER", "ACCEPT");
    }

    // Empty recipient list
    public static String inviteUser_002() {
        return generateInviteUserPayload("[]", "USER", "ACCEPT");
    }

    // Invalid email format
    public static String inviteUser_003() {
        return generateInviteUserPayload("[\"invalid.email\"]", "USER", "ACCEPT");
    }

    // Invalid invite type
    public static String inviteUser_004() {
        return generateInviteUserPayload("[\"avishi.sharma@roundcircle.tech\"]", "ADMIN", "ACCEPT");
    }

    // Invalid action
    public static String inviteUser_005() {
        return generateInviteUserPayload("[\"avishi.sharma@roundcircle.tech\"]", "USER", "DECLINE");
    }


}
