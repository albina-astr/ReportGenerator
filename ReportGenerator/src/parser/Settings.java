package parser;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Settings {
    @XmlElement(name = "page")
    private Page pages;
    @XmlElement(name = "columnsL")
    private Columns columns;

    public Settings() {
    }

    public Settings(Page pages, Columns columns) {
        this.pages = pages;
        this.columns = columns;
    }

    public Page getPages() {
        return pages;
    }

    public void setPages(Page pages) {
        this.pages = pages;
    }

    public Columns getColumns() {
        return columns;
    }

    public void setColumns(Columns columns) {
        this.columns = columns;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Page {
        @XmlElement(name = "width")
        private int width;
        @XmlElement(name = "height")
        private int height;

        public Page(int height, int width) {
            this.height = height;
            this.width = width;
        }

        public Page() {
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Column {
        @XmlElement(name = "title")
        private String title;
        @XmlElement(name = "width")
        private int width;

        public Column(String dataColumnLength, int fioColumnLength) {
            this.title = dataColumnLength;
            this.width = fioColumnLength;
        }

        public Column() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Columns {
        @XmlElement(name = "column")
        private List<Column> columnsL;

        public Columns(List<Column> columnsL) {
            this.columnsL = columnsL;
        }

        public Columns() {
        }

        public List<Column> getColumnsL() {
            return columnsL;
        }

        public void setColumnsL(List<Column> columnsL) {
            this.columnsL = columnsL;
        }
    }
}
