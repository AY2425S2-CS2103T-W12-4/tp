package tassist.address.storage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

/**
 * A utility class that handles the conversion between CSV and JSON formats.
 */
public class CsvJsonConverter {

    /**
     * Converts a CSV file to a JSON file.
     * <p>
     * This method reads a CSV file, processes its content, and converts it into a JSON format, which is then
     * saved to the provided JSON file path. The JSON is formatted with indentation for better readability.
     * </p>
     *
     * @param csvFilePath the path to the input CSV file to be converted
     * @param jsonFilePath the path to the output JSON file where the converted data will be saved
     * @throws IOException if there is an error reading the CSV file or writing the JSON file
     * @throws CsvException if there is an error during CSV parsing
     */
    public void convertCsvToJson(Path csvFilePath, Path jsonFilePath) throws IOException, CsvException {
        FileReader fileReader = new FileReader(csvFilePath.toString());
        CSVReader csvReader = new CSVReader(fileReader);

        List<String[]> rows = csvReader.readAll();

        String[] headers = rows.get(0);
        List<Map<String, Object>> data = retrieveData(headers, rows);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        Map<String, List<Map<String, Object>>> wrappedData = new LinkedHashMap<>();
        wrappedData.put("persons", data);
        wrappedData.put("timedEvents", new ArrayList<>());
        objectMapper.writeValue(new File(jsonFilePath.toString()), wrappedData);

        System.out.println("CSV has been successfully converted to JSON!");
    }

    private List<Map<String, Object>> retrieveData(String[] headers, List<String[]> rows) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            Map<String, Object> personData = new LinkedHashMap<>();

            for (int j = 0; j < headers.length; j++) {
                String key = headers[j];
                String value = row[j];

                if (key.equalsIgnoreCase("tags")) {
                    if (value == null || value.trim().isEmpty()) {
                        personData.put(key, new String[0]);
                    } else {
                        personData.put(key, Arrays.asList(value.split(",")));
                    }
                } else {
                    if (value == null || value.trim().isEmpty()) {
                        personData.put(key, "");
                    } else {
                        personData.put(key, value);
                    }
                }
            }

            data.add(personData);
        }

        return data;
    }
}
