package com.example.keeper;

public class Highlight {
    private String highlight;
    private Integer pageNumber;

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Highlight(String highlight, int pageNumber) {
        this.highlight = highlight;
        this.pageNumber = pageNumber;
    }
}
