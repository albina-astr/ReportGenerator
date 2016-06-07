package main;

import parser.Settings;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class Generator {
    public static void main(String[] args) {
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
    }
}
