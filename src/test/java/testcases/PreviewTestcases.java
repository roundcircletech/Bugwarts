package testcases;

import java.util.*;
import com.opencsv.CSVReader;
import java.io.FileReader;

import static constants.Urls.CLIENT_DETAILS_CSV;
import static files.Payload.generatePreviewPayload;
public class PreviewTestcases {

    public static List<String> getAllPreviewPayloads() throws Exception {
        List<String> payloads = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(CLIENT_DETAILS_CSV))) {
            String[] nextLine;
            reader.readNext();

            while ((nextLine = reader.readNext()) != null) {
                String clientId = nextLine[0];
                String origin = nextLine[1];
                String referer = nextLine[2];

                String payload = generatePreviewPayload(UUID.fromString(clientId), origin, referer);
                payloads.add(payload);
            }
        }
        return payloads;
    }
}