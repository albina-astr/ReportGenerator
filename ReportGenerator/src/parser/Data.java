package parser;

public class Data {
    private String number;
    private String date;
    private String fio;

    public Data() {
    }

    public Data(String colNumber, String fioColumn, String dateColumn) {
        this.number = colNumber;
        this.fio = fioColumn;
        this.date = dateColumn;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }
}
