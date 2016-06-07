package parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportMaker {
    private List<Data> data;
    private Settings settings;

    public ReportMaker() {
    }

    public ReportMaker(List<Data> data, Settings settings) {
        this.data = data;
        this.settings = settings;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void writeToFile(File file) throws IOException {
        final int PAGE_HEIGHT = Integer.parseInt(String.valueOf(settings.getPages().getHeight()));
        final int PAGE_WIDTH = Integer.parseInt(String.valueOf(settings.getPages().getWidth()));
        final char PAGE_END = '~';
        final String LINE_SEPARATOR = new String(new char[PAGE_WIDTH]).replace('\0', '_');

        // generate each column length (with 2 spaces)
        int[] columnLength = new int[settings.getColumns().getColumnsL().size()];
        for (int i = 0; i < columnLength.length; i++) {
            columnLength[i] = 2 + settings.getColumns().getColumnsL().get(i).getWidth();
        }

        // generate table HEADER
        //TODO:: check 
        StringBuilder HEADER = new StringBuilder(new String(new char[32]));
        int c = 0;
        for (int i = 0; i < columnLength.length; i++) {
            String title = settings.getColumns().getColumnsL().get(i).getTitle();
            HEADER.replace(c, c + columnLength[i], "| " + title);
            c += settings.getColumns().getColumnsL().get(i).getWidth() + 3;
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter wr = new FileWriter(file.getAbsolutePath());
        BufferedWriter bw = new BufferedWriter(wr);
        bw.write(HEADER.toString()); // first, write table header
        bw.write(LINE_SEPARATOR); // separate next line from header
        boolean isEndPage = false;
        int pageHeightCounter = 2; // 2 lines have already existed
        for (int i = 0; i < data.size() + 1; i++) {
            if (pageHeightCounter >= PAGE_HEIGHT) {
                isEndPage = true;
            }

            if (!isEndPage) {
                StringBuilder currentLine = new StringBuilder(PAGE_WIDTH);

            }
        }
    }
}
