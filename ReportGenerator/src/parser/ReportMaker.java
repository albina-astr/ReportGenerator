package parser;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReportMaker {
    private List<Data> dataList;
    private Settings settings;

    public ReportMaker() {
    }

    public ReportMaker(List<Data> dataList, Settings settings) {
        this.dataList = dataList;
        this.settings = settings;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    // method for writing all Data objects to text file
    public void writeToFile(File file) throws IOException {
        final int PAGE_HEIGHT = settings.getPages().getHeight();
        final int PAGE_WIDTH = settings.getPages().getWidth();
        final char PAGE_END = '~';
        final String LINE_SEPARATOR = StringUtils.repeat("-", PAGE_WIDTH);

        // generate each column length (with 2 whitespaces)
        int[] columnLength = new int[settings.getColumns().getColumnsList().size()];
        for (int i = 0; i < columnLength.length; i++) {
            columnLength[i] = 2 + settings.getColumns().getColumnsList().get(i).getWidth();
        }

        // generate table HEADER
        String HEADER = constructHeaderRecord(columnLength,PAGE_WIDTH);

        if (!file.exists()) {
            file.createNewFile();
        }

        int pageHeightCounter = 0;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), "UTF-16"));

        // iterate through each dataList object in list
        for (int i = 0; i < dataList.size(); i++) {
            // if page has free space
            if (pageHeightCounter <= PAGE_HEIGHT) {
                // if we start page writing
                if (pageHeightCounter == 0) {
                    // writing header with separating line
                    bw.write(HEADER.toString()); // first, write table header
                    bw.newLine();
                    bw.write(LINE_SEPARATOR); // separate next line from header
                    bw.newLine();
                    pageHeightCounter += 2;
                }
                // list for each line=record in document
                List<String> recordComponents = getRecord(dataList.get(i), PAGE_WIDTH);
                int components = recordComponents.size();
                // check whether it is possible to write on current page this record
                if (pageHeightCounter + components <= PAGE_HEIGHT) {
                    writeRecord(recordComponents, bw);
                    pageHeightCounter += components;

                    // if page has no free space
                    if (pageHeightCounter == PAGE_HEIGHT) {
                        bw.write(PAGE_END);
                        bw.newLine();
                        pageHeightCounter = 0; // ready to start new page writing
                    } else {
                        bw.write(LINE_SEPARATOR);
                        bw.newLine();
                        pageHeightCounter++;
                    }
                } // if page filled , we start new page
                else {
                    bw.write(PAGE_END);
                    bw.newLine();
                    pageHeightCounter = 0; // ready to start new page writing

                    bw.write(HEADER.toString()); // first, write table header
                    bw.newLine();
                    bw.write(LINE_SEPARATOR); // separate next line from header
                    bw.newLine();
                    pageHeightCounter += 2;

                    writeRecord(recordComponents, bw);
                    pageHeightCounter += components;

                    bw.write(LINE_SEPARATOR);
                    bw.newLine();
                    pageHeightCounter++;
                }
            }
        }

        bw.close();
    }

    // method for generating full string with num, date and fio
    private List<String> getRecord(Data data, int PAGE_WIDTH) throws IOException {
        final String SEPARATOR1 = " ";
        final String SEPARATOR2 = "/";

        String number = data.getNumber();
        String date = data.getDate();
        String fio = data.getFio();

        List<String> recordComponents = new ArrayList<>(); // list for one record (can consist of few strings)

        // lists for separated (if required) components of each line in source-data
        List<String> numberParts = divideStringByLimit(number, this.settings.getColumns().getColumnsList().get(0).getWidth(), SEPARATOR1); // TODO::check!!!
        List<String> dateParts = divideStringByLimit(date, this.settings.getColumns().getColumnsList().get(1).getWidth(), SEPARATOR2);
        List<String> fioParts = divideStringByLimit(fio, this.settings.getColumns().getColumnsList().get(2).getWidth(), SEPARATOR1);

        // align all strings for convinient writing to file (all lists have the same size)
        alignRecordComponents(numberParts, dateParts, fioParts);

        // add each line as component to record
        for (int i = 0; i < numberParts.size(); i++) {
            recordComponents.add(constructRecord(numberParts.get(i), dateParts.get(i), fioParts.get(i), PAGE_WIDTH));
        }

        return recordComponents;
    }

    // method inserts empty strings to every column list for easy record constructing
    private void alignRecordComponents(List<String> numberParts, List<String> dateParts, List<String> fioParts) {
        String numColPrototype = StringUtils.repeat(' ', settings.getColumns().getColumnsList().get(0).getWidth());
        String dateColPrototype = StringUtils.repeat(' ', settings.getColumns().getColumnsList().get(1).getWidth());
        String fioColPrototype = StringUtils.repeat(' ', settings.getColumns().getColumnsList().get(2).getWidth());

        // get max size of lists
        int alignParameter = dateParts.size() > fioParts.size() ? dateParts.size() : fioParts.size();
        for (int i = 0; i < alignParameter; i++) {
            // for each list: add empty string if it is necessary
            if (numberParts.size() < alignParameter) {
                numberParts.add(numColPrototype);
            }
            if (dateParts.size() < alignParameter) {
                dateParts.add(dateColPrototype);
            }
            if (fioParts.size() < alignParameter) {
                fioParts.add(fioColPrototype);
            }
        }
    }

    // method writes record (line with 3 columns) to result file
    private void writeRecord(List<String> recordComponents, BufferedWriter bw) {
        try {
            for (int i = 0; i < recordComponents.size(); i++) {
                bw.write(recordComponents.get(i));
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // method creates header string
    private String constructHeaderRecord(int[] columnLength, int LIMIT) {
        String prototype = StringUtils.repeat('\0', LIMIT);
        StringBuilder record = new StringBuilder(prototype);
        int c = 0;
        int n = columnLength.length;
        for (int i = 0; i < n; i++) {
            String title = settings.getColumns().getColumnsList().get(i).getTitle();
            record.replace(c, c + title.length() + 2, "| " + title);
            c += settings.getColumns().getColumnsList().get(i).getWidth() + 3;
            if (i == n - 1) {
                record.replace(c - 1, LIMIT - 1, " |");
                record.deleteCharAt(LIMIT);
            }
        }

        return record.toString();
    }

    // method creates record using words for each column
    private String constructRecord(String number, String date, String fio, int PAGE_WIDTH) {
        int c = 0;
        String prototype = StringUtils.repeat('\0', PAGE_WIDTH);
        StringBuilder record = new StringBuilder(prototype);
        record.replace(c, c + number.length() + 2, "| " + number);
        c += number.length() + 3;
        record.replace(c, c + date.length() + 2, "| " + date);
        c += date.length() + 3;
        record.replace(c, c + fio.length() + 2, "| " + fio);
        c += fio.length() + 3;
        record.replace(c - 1, PAGE_WIDTH - 1, " |");
        record.deleteCharAt(PAGE_WIDTH);

        return record.toString();
    }

    // method divides string via splitting and diving each word into parts (for fio and date)
    private List<String> divideStringByLimit(String stringToDivide, int LIMIT, String SEPARATOR) {
        List<String> dividedStringParts = new ArrayList<>();

        // if string.length() less or equal to LIMIT
        if (stringToDivide.length() <= LIMIT) {
            dividedStringParts.add(stringToDivide + StringUtils.repeat(" ", LIMIT - stringToDivide.length()));
        } else {
            String[] splitStr = stringToDivide.split(SEPARATOR);
            for (int i = 0; i < splitStr.length; i++) {
                // if element more than LIMIT divide it into word parts
                if (splitStr[i].length() > LIMIT) {
                    dividedStringParts.addAll(divideWordByLimit(splitStr[i], LIMIT));
                } else {
                    // try to add two first elements of splitted string together(in case if their length less or equal to LIMIT)
                    if (i != splitStr.length - 1) {
                        int twoWordsLength = splitStr[i].length() + splitStr[i + 1].length();
                        if (twoWordsLength < LIMIT) {
                            dividedStringParts.add(splitStr[i].concat(SEPARATOR).concat(splitStr[i + 1]).concat(SEPARATOR).
                                    concat(StringUtils.repeat(" ", LIMIT - twoWordsLength - 2)));
                            i++;
                        } else {
                            // else add only current array element
                            dividedStringParts.add(splitStr[i] + StringUtils.repeat(" ", LIMIT - splitStr[i].length()));
                        }
                    } else {
                        dividedStringParts.add(splitStr[i] + StringUtils.repeat(" ", LIMIT - splitStr[i].length()));
                    }
                }
            }
        }

        return dividedStringParts;
    }

    // method for dividing one word into few parts
    private List<String> divideWordByLimit(String s, int LIMIT) {
        List<String> wordParts = new ArrayList<>();
        // if word length less than LIMIT
        if (s.length() < LIMIT) {
            wordParts.add(s + StringUtils.repeat(" ", LIMIT - s.length()));
        } else {
            // get word substring of the LIMIT size
            wordParts.add(s.substring(0, LIMIT));
            // get second word part
            String second = s.substring(LIMIT, s.length());
            // if second part less than LIMIT
            if (second.length() < LIMIT) {
                wordParts.add(second + StringUtils.repeat(" ", LIMIT - second.length()));
            } else {
                // divide second part again
                wordParts.addAll(divideWordByLimit(second, LIMIT));
            }
        }

        return wordParts;
    }
}
