package files;

import org.json.JSONObject;
import java.util.*;

import static constants.Headers.*;
import static constants.Strings.*;

public class Payload {
    public static String generatePreviewPayload(UUID clientId, String origin, String referer){
        if(clientId != null) {
            JSONObject requestBody = new JSONObject();
            requestBody.put(CLIENT_ID, clientId.toString());
            requestBody.put(ORIGIN, origin);
            requestBody.put(REFERER, referer);
            return requestBody.toString();
        }
        else return EMPTY_STRING;
    }

}