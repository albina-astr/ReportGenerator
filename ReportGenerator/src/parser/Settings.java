package parser;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Settings {
    private Page pages;
    private Column columns;

    public Settings() {
    }

    public Settings(Page pages, Column columns) {
        this.pages = pages;
        this.columns = columns;
    }

    public Page getPages() {
        return pages;
    }

    @XmlElement(name = "page")
    public void setPages(Page pages) {
        this.pages = pages;
    }

    public Column getColumns() {
        return columns;
    }

    @XmlElement(name = "columns")
    public void setColumns(Column columns) {
        this.columns = columns;
    }

    private class Page {
        private int width;
        private int height;

        private Page(int height, int width) {
            this.height = height;
            this.width = width;
        }

        private Page() {
        }

        public int getWidth() {
            return width;
        }

        @XmlAttribute(name = "width")
        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        @XmlAttribute(name = "height")
        public void setHeight(int height) {
            this.height = height;
        }
    }

    private class Column {
        private String title;
        private int width;
        private List<Column> columns;

        private Column(String dataColumnLength, int fioColumnLength) {
            this.title = dataColumnLength;
            this.width = fioColumnLength;
        }

        private Column() {
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

        @XmlElement(name="columns")
        public List<Column> getColumns() {
            return columns;
        }

        public void setColumns(List<Column> columns) {
            this.columns = columns;
        }
    }
}
