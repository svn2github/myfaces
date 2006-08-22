package org.apache.myfaces.examples.colspanexample;

public class Cell {
    private String text;
    public Cell(String text) {
        super();
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
