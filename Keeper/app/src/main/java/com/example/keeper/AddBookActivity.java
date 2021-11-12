package com.example.keeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddBookActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private ArrayList<Book> bookInfoArrayList;
    private SearchedBookAdapter adapter;
    private SearchView addBookSearchView;
    private TextView noResultLabel;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.searchedBookRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();

        bookInfoArrayList = new ArrayList<>();
        adapter = new SearchedBookAdapter(AddBookActivity.this, bookInfoArrayList);

        recyclerView.setAdapter(adapter);


        noResultLabel = findViewById(R.id.noResultLabel);
        loadingBar = findViewById(R.id.progressBar);

        addBookSearchView = findViewById(R.id.addBookSearchView);


        addBookSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchBooks(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    private void fetchBooks(String keyword) {
        bookInfoArrayList.clear();
        adapter.notifyDataSetChanged();
        noResultLabel.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.VISIBLE);
        bookInfoArrayList.clear();

        String url = "https://www.googleapis.com/books/v1/volumes?q="+keyword;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray itemsArray = response.getJSONArray("items");

                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.optJSONObject(i);
                        JSONObject volumeObj = itemsObj.optJSONObject("volumeInfo");
                        String title = (volumeObj.optString("title") == null) ? "Title unavailable" : volumeObj.optString("title");
                        JSONArray authorsArray = volumeObj.optJSONArray("authors");
                        String publishedDate = (volumeObj.optString("publishedDate") == null) ? "Date Unavailable" : volumeObj.optString("publishedDate");
                        JSONArray genreList = (volumeObj.optJSONArray("categories"));

                        String genre = "";
                        if (genreList == null) {
                            genre = "N/A";
                        } else {
                            for (int k = 0; k < 1; k++) {
                                genre += (genreList.getString(k));
                            }
                        }

                        JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                        String thumbnail = "";
                        if (imageLinks != null) {
                            thumbnail = imageLinks.optString("thumbnail").replace("http", "https");
                        }


                        String description = (volumeObj.optString("description") == null || volumeObj.optString("description") == "") ? "No description available." : volumeObj.optString("description");

                        String language = (volumeObj.optString("language") == null) ? "N/A" : volumeObj.optString("language");

                        String authors = "";
                        if (authorsArray != null) {
                            for (int j = 0; j < authorsArray.length(); j++) {
                                authors += (authorsArray.getString(j)) + ", ";
                            }


                            // remove the last comma
                            if (authors.endsWith(", ")) {
                                authors = authors.substring(0, authors.length() - 2);
                            }
                            // for odd cases where one author has a comma
                             if (authors.endsWith(",")) {
                                authors = authors.substring(0, authors.length() - 1);
                            }
                        } else {
                            authors = "Author unknown";
                        }

                            // only get the year
                            publishedDate = publishedDate.split("-")[0];


                        Book bookInfo = new Book(title, authors, publishedDate, thumbnail, description, genre, language);
                        bookInfoArrayList.add(bookInfo);
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loadingBar.setVisibility(View.INVISIBLE);
                if (bookInfoArrayList.isEmpty()) {
                    bookInfoArrayList.clear();
                    adapter.notifyDataSetChanged();
                    noResultLabel.setText("No results found");
                    noResultLabel.setVisibility(View.VISIBLE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: "+error,Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }





}