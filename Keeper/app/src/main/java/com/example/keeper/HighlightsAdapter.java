package com.example.keeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HighlightsAdapter extends RecyclerView.Adapter<HighlightsAdapter.HighlightsViewHolder> {

    private final Context context;
    private List<Highlight> highlightList;

    public HighlightsAdapter(Context context, List<Highlight> highlights) {
        this.context = context;
        highlightList = highlights;
    }

    @NonNull
    @Override
    public HighlightsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.highlight, parent, false);
        return new HighlightsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightsViewHolder holder, int position) {
        Highlight highlight = highlightList.get(position);

        holder.highlightTextView.setText('\"' + highlight.getHighlight() + '\"');
        holder.pageNumberTextView.setText("On page " + highlight.getPageNumber().toString());
    }

    @Override
    public int getItemCount() {
        return highlightList.size();
    }

    public class HighlightsViewHolder extends RecyclerView.ViewHolder {
        TextView highlightTextView, pageNumberTextView;
        RelativeLayout relativeLayout;

        public HighlightsViewHolder(@NonNull View itemView) {
            super(itemView);
            highlightTextView = itemView.findViewById(R.id.highlight);
            pageNumberTextView = itemView.findViewById(R.id.pageNumber);

            relativeLayout = itemView.findViewById(R.id.highlightCard);

        }
    }

}
