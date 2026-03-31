package com.example.lab4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BookStoreAdapter extends ArrayAdapter<BookStore> {

    private final Context context;
    private final ArrayList<BookStore> stores;

    public BookStoreAdapter(Context context, ArrayList<BookStore> stores) {
        super(context, 0, stores);
        this.context = context;
        this.stores = stores;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bookstore, parent, false);

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvBooks = view.findViewById(R.id.tvBooks);
        TextView tvType = view.findViewById(R.id.tvType);
        TextView tvProgram = view.findViewById(R.id.tvProgram);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvDate = view.findViewById(R.id.tvDate);

        BookStore store = stores.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        tvName.setText(getContext().getString(R.string.bookstore_name) + ": " + store.getName());
        tvBooks.setText(getContext().getString(R.string.number_of_books) + ": " + store.getNumberOfBooks());
        tvType.setText("Type: " + store.getStoreType().name());
        tvProgram.setText(getContext().getString(R.string.open_24h) + ": " + (store.isOpen24h() ? "Yes" : "No"));
        tvPrice.setText(getContext().getString(R.string.average_price) + ": " + store.getAveragePrice());
        tvDate.setText("Date: " + sdf.format(store.getOpeningDate()));

        return view;
    }
}