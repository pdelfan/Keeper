package com.example.keeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SearchedBookAdapter extends RecyclerView.Adapter<SearchedBookAdapter.SearchedBookHolder> {

    private Context context;
    private List<Book> bookList;

    public SearchedBookAdapter(Context context, List<Book> books) {
        this.context = context;
        bookList = books;
    }

    @NonNull
    @Override
    public SearchedBookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.searched_book, parent, false);
        return new SearchedBookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchedBookHolder holder, int position) {
        Book book = bookList.get(position);

        if (book.getCover().isEmpty()) {
            Glide.with(context).load(context.getDrawable(R.drawable.ic_baseline_image_not_supported_120)).into(holder.cover);
        } else {
            Glide.with(context).load(book.getCover()).into(holder.cover);
        }

        holder.title.setText(book.getTitle());
        holder.authors.setText(book.getAuthors());
        holder.date.setText(book.getPublishedDate());
        holder.genre.setText(book.getGenre());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookDetailActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("cover", book.getCover());
                bundle.putString("title", book.getTitle());
                bundle.putString("authors", book.getAuthors());
                bundle.putString("date", book.getPublishedDate());
                bundle.putString("description", book.getDescription());
                bundle.putString("genre", book.getGenre());
                bundle.putString("language", book.getLanguage());

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class SearchedBookHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView title, authors, date, genre;
        RelativeLayout relativeLayout;

        public SearchedBookHolder(@NonNull View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.bookCover);
            title = itemView.findViewById(R.id.bookTitle);
            authors = itemView.findViewById(R.id.bookAuthors);
            date = itemView.findViewById(R.id.bookDate);
            genre = itemView.findViewById(R.id.searchedGenre);

            relativeLayout = itemView.findViewById(R.id.searchedBookCard);
        }
    }
}
