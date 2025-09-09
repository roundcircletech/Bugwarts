package api;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.opencsv.CSVReader;
import java.io.FileReader;
import static api.Payload.generatePreviewPayload;
public class PreviewTestcases {

    public static List<String> getAllPreviewPayloads() throws Exception {
        List<String> payloads = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader("src/test/resources/clientDetails.csv"))) {
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
    }}
