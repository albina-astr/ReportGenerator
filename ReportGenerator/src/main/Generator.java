package main;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import parser.Data;
import parser.ReportMaker;
import parser.Settings;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Generator {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) throws IOException {
        File xmlSettingsFile = new File(args[0]);
        File dataFile = new File(args[1]);
        File resultFile = new File(args[2]);

        List<Data> data = new ArrayList<>();
        Settings settings = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            settings = (Settings) jaxbUnmarshaller.unmarshal(xmlSettingsFile);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        CsvToBean csv = new CsvToBean();

        List dataObjects;
        try {
            CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(dataFile.getName()), "UTF-16"), '\t');

            //Set column mapping strategy
            dataObjects = csv.parse(setColumnMapping(), csvReader);

            for (Object object : dataObjects) {
                Data curData = (Data) object;
                data.add(curData);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        ReportMaker reportMaker = new ReportMaker(data, settings);
        reportMaker.writeToFile(resultFile);

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static ColumnPositionMappingStrategy setColumnMapping() {
        ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
        strategy.setType(Data.class);
        String[] columns = new String[]{"number", "date", "fio"};
        strategy.setColumnMapping(columns);
        return strategy;
    }
}
