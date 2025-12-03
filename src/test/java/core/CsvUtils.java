package core;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CsvUtils {

    /**
     * Reads CSV file and returns all rows as a list of string arrays
     * @param filePath Path to the CSV file
     * @param skipHeader Whether to skip the first row (header)
     * @return List of string arrays representing rows
     */
    public static List<String[]> readCsv(String filePath, boolean skipHeader) throws Exception {
        List<String[]> rows = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            if (skipHeader) {
                reader.readNext(); // skip header
            }
            String[] row;
            while ((row = reader.readNext()) != null) {
                rows.add(row);
            }
        }
        return rows;
    }

    /**
     * Reads CSV file and extracts specific column as list
     * @param filePath Path to the CSV file
     * @param columnIndex Index of the column to extract
     * @param skipHeader Whether to skip the first row (header)
     * @param filter Whether to filter empty/null values
     * @return List of strings from the specified column
     */
    public static List<String> readCsvColumn(String filePath, int columnIndex, boolean skipHeader, boolean filter) throws Exception {
        List<String> values = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            if (skipHeader) {
                reader.readNext();
            }
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length > columnIndex) {
                    String value = row[columnIndex];
                    if (filter) {
                        if (value != null && !value.trim().isEmpty()) {
                            values.add(value.trim());
                        }
                    } else {
                        values.add(value);
                    }
                }
            }
        }
        return values;
    }

    /**
     * Processes CSV rows and transforms them into desired objects
     * @param filePath Path to the CSV file
     * @param skipHeader Whether to skip the first row (header)
     * @param processor Function to process each row
     * @return List of processed objects
     */
    public static <T> List<T> processCsvRows(String filePath, boolean skipHeader, Function<String[], T> processor) throws Exception {
        List<T> results = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            if (skipHeader) {
                reader.readNext();
            }
            String[] row;
            while ((row = reader.readNext()) != null) {
                T result = processor.apply(row);
                if (result != null) {
                    results.add(result);
                }
            }
        }
        return results;
    }

    /**
     * Converts a list to TestNG data provider format (Object[][])
     * @param list List to convert
     * @return Object[][] suitable for TestNG @DataProvider
     */
    public static Object[][] toDataProvider(List<?> list) {
        return list.stream()
                .map(item -> new Object[]{item})
                .toArray(Object[][]::new);
    }

    /**
     * Converts a list using a custom mapper to TestNG data provider format
     * @param list List to convert
     * @param mapper Function to map each item to Object[]
     * @return Object[][] suitable for TestNG @DataProvider
     */
    public static <T> Object[][] toDataProvider(List<T> list, Function<T, Object[]> mapper) {
        return list.stream()
                .map(mapper)
                .toArray(Object[][]::new);
    }
}

