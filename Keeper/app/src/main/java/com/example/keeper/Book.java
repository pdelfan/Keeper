package com.example.keeper;


import java.util.ArrayList;

public class Book {
    private String id;
    private String title;
    private String subtitle;
    private String authors;
    private String publishedDate;
    private String cover;
    private String language;
    private String genre;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    // constructor for the searched book
    public Book(String title, String authors, String publishedDate, String cover, String description, String genre, String language) {
        this.title = title;
        this.authors = authors;
        this.publishedDate = publishedDate;
        this.cover = cover;
        this.description = description;
        this.genre = genre;
        this.language = language;
    }

    // constructor for the saved book
    public Book (String id, String title, String authors, String publishedDate, String cover) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.publishedDate = publishedDate;
        this.cover = cover;
    }


}
