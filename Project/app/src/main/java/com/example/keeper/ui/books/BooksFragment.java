package com.example.keeper.ui.books;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.keeper.R;

public class BooksFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View bookView = inflater.inflate(R.layout.fragment_books, container, false);

        return bookView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}