package com.example.keeper;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LibraryBookAdapter extends RecyclerView.Adapter<LibraryBookAdapter.LibraryBooksViewHolder> implements Filterable {

    private final Context context;
    private List<Book> bookList;
    private List<Book> bookListAll;

    public LibraryBookAdapter(Context context, List<Book> books) {
        this.context = context;
        bookList = books;
        this.bookListAll = new ArrayList<>(bookList);
    }

    @NonNull
    @Override
    public LibraryBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.library_book, parent, false);
        return new LibraryBooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryBooksViewHolder holder, int position) {
        Book book = bookList.get(position);

        if (book.getCover().isEmpty()) {
            Glide.with(context).load(context.getDrawable(R.drawable.ic_baseline_image_not_supported_120)).into(holder.coverImageView);
        } else {
            Glide.with(context).load(book.getCover()).into(holder.coverImageView);
        }


        holder.titleTextView.setText(book.getTitle());
        holder.authorsTextView.setText(book.getAuthors());
        holder.dateTextView.setText(book.getPublishedDate());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HighlightsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("id", book.getId());

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class LibraryBooksViewHolder extends RecyclerView.ViewHolder {

        ImageView coverImageView;
        TextView titleTextView, authorsTextView, dateTextView;
        RelativeLayout relativeLayout;

        public LibraryBooksViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImageView = itemView.findViewById(R.id.bookCover);
            titleTextView = itemView.findViewById(R.id.bookTitle);
            authorsTextView = itemView.findViewById(R.id.bookAuthors);
            dateTextView = itemView.findViewById(R.id.bookDate);

            relativeLayout = itemView.findViewById(R.id.libraryBookCard);

        }
    }


    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        // in background
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Book> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(bookListAll);
            } else {
                for (Book book : bookListAll) {
                    if (book.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase()) || book.getAuthors().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(book);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        // on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            bookList.clear();
            bookList.addAll((Collection<? extends Book>) filterResults.values);
            notifyDataSetChanged();
        }
    };











}
