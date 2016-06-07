package main;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import parser.Data;
import parser.Settings;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.List;

public class Generator {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) throws IOException {
        File xmlSettingsFile = new File(args[0]);
        File dataFile = new File(args[1]);
        File resultFile = new File(args[2]);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Settings settings = (Settings) jaxbUnmarshaller.unmarshal(xmlSettingsFile);
            System.out.println(settings);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        CsvToBean csv = new CsvToBean();

        List list;
        try {
            CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(dataFile.getName()), "UTF-16"), '\t');

            //Set column mapping strategy
            list = csv.parse(setColumnMapping(), csvReader);

            for (Object object : list) {
                Data employee = (Data) object;
                System.out.println(employee);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

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
