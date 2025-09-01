package Utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {

    // Read Excel file and return data in Object[][] format
    public static Object[][] getExcelData(String filePath) throws IOException {
        List<String> urls = new ArrayList<>();

        // Opening Excel file
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        // Created iterator for rows
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); //skip header row

        // Loop through remaining rows
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell cell = row.getCell(1);  // getting column B (index 1)
            urls.add(cell.getStringCellValue());  // adding URL to list
        }

        workbook.close();
        fis.close();

        // Convert list of URLs into Object[][] for DataProvider
        Object[][] data = new Object[urls.size()][1];
        for (int i = 0; i < urls.size(); i++) {
            data[i][0] = urls.get(i);
        }
        return data;
    }
}
