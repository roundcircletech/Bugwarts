package testcases;

import static constants.Strings.*;
import static files.Payload.generateBookDemoPayload;

public class BookDemoTestCases {

    // Valid booking
    public static String bookDemo_001() {
        return generateBookDemoPayload(VALID_CONVERSATION_ID, "Asana", "CONVERSATION", "Test User",
                "test.user@example.com", "2025-03-07T05:00:00.000Z",
                "2025-03-07T05:30:00.000Z", "91 3456789678");
    }

    // Missing toolName
    public static String bookDemo_002() {
        return generateBookDemoPayload(VALID_CONVERSATION_ID, EMPTY_STRING, "CONVERSATION", "Test User",
                "test.user@example.com", "2025-03-07T05:00:00.000Z",
                "2025-03-07T05:30:00.000Z", "91 3456789678");
    }

    // Invalid email format
    public static String bookDemo_003() {
        return generateBookDemoPayload(VALID_CONVERSATION_ID, "Asana", "CONVERSATION", "Test User",
                "invalid-email", "2025-03-07T05:00:00.000Z",
                "2025-03-07T05:30:00.000Z", "91 3456789678");
    }

    // Empty name field
    public static String bookDemo_004() {
        return generateBookDemoPayload(VALID_CONVERSATION_ID, "Asana", "CONVERSATION", EMPTY_STRING,
                "test.user@example.com", "2025-03-07T05:00:00.000Z",
                "2025-03-07T05:30:00.000Z", "91 3456789678");
    }

    // Unauthorized request (Handled in Test Class)
    public static String bookDemo_005() {
        return bookDemo_001();
    }

    // Null source
    public static String bookDemo_006() {
        return generateBookDemoPayload(VALID_CONVERSATION_ID, "Asana", null, "Test User",
                "test.user@example.com", "2025-03-07T05:00:00.000Z",
                "2025-03-07T05:30:00.000Z", "91 3456789678");
    }

    // Invalid phone format
    public static String bookDemo_007() {
        return generateBookDemoPayload(VALID_CONVERSATION_ID, "Asana", "CONVERSATION", "Test User",
                "test.user@example.com", "2025-03-07T05:00:00.000Z",
                "2025-03-07T05:30:00.000Z", "invalid-phone");
    }

    // Empty start time
    public static String bookDemo_008() {
        return generateBookDemoPayload(VALID_CONVERSATION_ID, "Asana", "CONVERSATION", "Test User",
                "test.user@example.com", EMPTY_STRING,
                "2025-03-07T05:30:00.000Z", "91 3456789678");
    }

    // Start time after end time
    public static String bookDemo_009() {
        return generateBookDemoPayload(VALID_CONVERSATION_ID, "Asana", "CONVERSATION", "Test User",
                "test.user@example.com", "2025-03-07T06:00:00.000Z",
                "2025-03-07T05:30:00.000Z", "91 3456789678");
    }

    // End time missing
    public static String bookDemo_010() {
        return generateBookDemoPayload(VALID_CONVERSATION_ID, "Asana", "CONVERSATION", "Test User",
                "test.user@example.com", "2025-03-07T05:00:00.000Z",
                EMPTY_STRING, "91 3456789678");
    }

    // Special characters in name
    public static String bookDemo_011() {
        return generateBookDemoPayload(VALID_CONVERSATION_ID, "Asana", "CONVERSATION", "@Test!User#",
                "test.user@example.com", "2025-03-07T05:00:00.000Z",
                "2025-03-07T05:30:00.000Z", "91 3456789678");
    }

    // Invalid source type
    public static String bookDemo_012() {
        return generateBookDemoPayload(VALID_CONVERSATION_ID, "Asana", "INVALID_SOURCE", "Test User",
                "test.user@example.com", "2025-03-07T05:00:00.000Z",
                "2025-03-07T05:30:00.000Z", "91 3456789678");
    }

    // Long comment in name field
    public static String bookDemo_013() {
        return generateBookDemoPayload(VALID_CONVERSATION_ID, "Asana", "CONVERSATION",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                        "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "test.user@example.com", "2025-03-07T05:00:00.000Z",
                "2025-03-07T05:30:00.000Z", "91 3456789678");
    }

    // Invalid time format
    public static String bookDemo_014() {
        return generateBookDemoPayload(VALID_CONVERSATION_ID, "Asana", "CONVERSATION", "Test User",
                "test.user@example.com", "invalid-time",
                "2025-03-07T05:30:00.000Z", "91 3456789678");
    }

    // Mix of invalid values
    public static String bookDemo_015() {
        return generateBookDemoPayload(INVALID_CONVERSATION_ID, EMPTY_STRING, "INVALID_SOURCE",
                "12345", "invalid-email", "invalid-time",
    }

}
