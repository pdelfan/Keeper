package com.example.keeper;

public class Highlight {
    private String id;
    private String highlight;
    private Integer pageNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Highlight(String id, String highlight, int pageNumber) {
        this.id = id;
        this.highlight = highlight;
        this.pageNumber = pageNumber;
    }
}
