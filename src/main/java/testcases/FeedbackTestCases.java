package testcases;

import static constants.Strings.*;
import static files.Payload.generateFeedbackPayload;

public class FeedbackTestCases {

    // Valid feedback
    public static String feedback_001() {
        return generateFeedbackPayload(VALID_CONVERSATION_ID, LIKE, "Great conversation!");
    }

    // Missing conversationId
    public static String feedback_002() {
        return generateFeedbackPayload(EMPTY_STRING, LIKE, "Missing conversationId.");
    }

    // Invalid status
    public static String feedback_003() {
        return generateFeedbackPayload(VALID_CONVERSATION_ID, INVALID_STATUS, "Invalid status value.");
    }

    // Empty comment (valid case)
    public static String feedback_004() {
        return generateFeedbackPayload(VALID_CONVERSATION_ID, LIKE, EMPTY_STRING);
    }

    // Feedback with a null status
    public static String feedback_005() {
        return generateFeedbackPayload(VALID_CONVERSATION_ID, null, "Null status field.");
    }

    // Feedback with an empty status
    public static String feedback_007() {
        return generateFeedbackPayload(VALID_CONVERSATION_ID, EMPTY_STRING, "Empty status value.");
    }

    // Feedback with an invalid conversationId format
    public static String feedback_008() {
        return generateFeedbackPayload(INVALID_CONVERSATION_ID, LIKE, "Invalid conversationId format.");
    }

    // Feedback with a non-existent conversationId
    public static String feedback_009() {
        return generateFeedbackPayload(NON_EXISTENT_CONVERSATION_ID, LIKE, "Non-existent conversation.");
    }

    // Feedback with a non-allowed status (e.g., "DISLIKE" if only "LIKE" and "DISLIKE" are allowed)
    public static String feedback_010() {
        return generateFeedbackPayload(VALID_CONVERSATION_ID, INVALID_STATUS, "Invalid status option.");
    }

    // Feedback with a mix of valid and invalid values
    public static String feedback_011() {
        return generateFeedbackPayload(INVALID_CONVERSATION_ID, INVALID_STATUS, EMPTY_STRING);
    }

}
