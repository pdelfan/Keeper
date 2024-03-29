package com.example.keeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private ArrayList<Book> books;
    private ArrayList<Highlight> highlights;

    private Context context;
    private static final String DATABASE_NAME = "Library.db";
    private static final int DATABASE_VERSION = 1;

    // tables
    private static final String TABLE_BOOKS = "books";
    private static final String TABLE_HIGHLIGHTS = "highlights";

    // common column names

    // BOOKS Table
    private static final String BOOK_ID = "book_id";
    private static final String TITLE = "title";
    private static final String AUTHORS = "authors";
    private static final String YEAR = "year";
    private static final String COVER = "cover";
    private static final String CREATE_TABLE_BOOKS = "CREATE TABLE " + TABLE_BOOKS + " (" + BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT, " + AUTHORS + " TEXT, " + YEAR + " TEXT, " + COVER + " TEXT);";

    // HIGHLIGHTS Table
    private static final String NOTE_ID = "note_id";
    private static final String HIGHLIGHT = "highlight";
    private static final String PAGE_NUMBER = "page_number";
    private static final String CREATE_TABLE_HIGHLIGHTS = "CREATE TABLE " + TABLE_HIGHLIGHTS + " (" + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BOOK_ID + " INTEGER, " + HIGHLIGHT + " TEXT, " + PAGE_NUMBER + " INTEGER, " + "FOREIGN KEY " + "(" + BOOK_ID + ")" + " REFERENCES " + TABLE_BOOKS + " (" + BOOK_ID + "));";


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BOOKS);
        db.execSQL(CREATE_TABLE_HIGHLIGHTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHLIGHTS);
        onCreate(db);
    }

    public void addBook(String title, String authors, String year, String cover) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TITLE, title);
        cv.put(AUTHORS, authors);
        cv.put(YEAR, year);
        cv.put(COVER, cover);

        long result = db.insert(TABLE_BOOKS, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Couldn't add the book to your library", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added the book to your library", Toast.LENGTH_SHORT).show();
        }
    }


    public ArrayList<Book> getAllBooks() {
        books = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_BOOKS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {
            do {
                String id = cursor.getString(0);
                String title = cursor.getString(1);
                String authors = cursor.getString(2);
                String year = cursor.getString(3);
                String cover = cursor.getString(4);

                Book book = new Book(id, title, authors, year, cover);
                books.add(book);
            } while (cursor.moveToNext());
        }
        db.close();
        return books;
    }


    public boolean deleteBook(String bookID) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_BOOKS,  "book_id=?", new String[]{bookID});
        if (result == -1) {
            Toast.makeText(context, "Couldn't delete the book", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(context, "Deleted the book", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public void addHighlight(String bookID, String highlight, Integer pageNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(BOOK_ID, bookID);
        cv.put(HIGHLIGHT, highlight);
        cv.put(PAGE_NUMBER, pageNumber);

        long result = db.insert(TABLE_HIGHLIGHTS, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Couldn't add the hilighlight to your library", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added the highlight to your library", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Highlight> getAllHighlights(String bookID) {
        highlights = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_HIGHLIGHTS + " where " + "book_id" +"="+bookID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {
            do {
                String highlightID = cursor.getString(0);
                String highlightText = cursor.getString(2);
                Integer pageNumber = Integer.parseInt(cursor.getString(3));

                Highlight highlight = new Highlight(highlightID, highlightText, pageNumber);
                highlights.add(highlight);
            } while (cursor.moveToNext());
        }
        db.close();
        return highlights;
    }

    public boolean deleteHighlight(String highlightID) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_HIGHLIGHTS,  "note_id=?", new String[]{highlightID});
        if (result == -1) {
            Toast.makeText(context, "Couldn't delete the highlight(s)", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(context, "Deleted the highlight(s)", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public boolean deleteAllHighlights(String bookID) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_HIGHLIGHTS,  "book_id=?", new String[]{bookID});
        if (result == -1) {
            Toast.makeText(context, "Couldn't delete all the highlight(s)", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(context, "Deleted all the highlight(s)", Toast.LENGTH_SHORT).show();
            return true;
        }
    }


}
