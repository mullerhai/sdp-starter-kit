package io.pravega.inject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple data generator for  generating CSV data to JSON and  the data will be used in samples.
 */
public class DataGenerator {
    // Logger initialization
    private static final Logger LOG = LoggerFactory.getLogger(DataGenerator.class);

    /**
     *  Read CSV file and generate  JSON  String as json array of all rows.
     */
    public static String convertCsvToJson(String filePath) throws Exception {
//        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//        InputStream inputStream = classloader.getResourceAsStream(fileName);
        File dataFile = new File(filePath);
        InputStream inputStream = new FileInputStream(dataFile);
        CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
        CsvMapper csvMapper = new CsvMapper();

        // Read data from CSV file
        List<Object> readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(inputStream).readAll();

        ObjectMapper mapper = new ObjectMapper();

        // Write JSON formated data to stdout
        String result = mapper.writeValueAsString(readAll);

        LOG.debug("@@@@@@@@@@@@@ DATA  @@@@@@@@@@@@@  "+result);

        return result;
    }

    // get file from classpath, resources folder
    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }
}