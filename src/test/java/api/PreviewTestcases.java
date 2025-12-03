package api;
import core.CsvUtils;
import java.util.List;
import java.util.UUID;
import static api.Payload.generatePreviewPayload;
import static constants.Urls.CLIENT_DETAILS_CSV;

public class PreviewTestcases {

    public static List<String> getAllPreviewPayloads() throws Exception {
        return CsvUtils.processCsvRows(CLIENT_DETAILS_CSV, true, row ->
                generatePreviewPayload(UUID.fromString(row[0]), row[1], row[2])
        );
    }}
