package testcases;

import static files.Payload.generateAnonymousV2Payload;
import static files.ReusableMethods.sessionId;

public class AnonymousV2Testcases {

    public static String anonymousV2_001() {
        return generateAnonymousV2Payload(sessionId());
    }

    public static String anonymousV2_002() {
        return generateAnonymousV2Payload(null);
    }
}
