package com.example.lab8;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookstores")
public class BookStore {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;

    @NonNull
    public String type;

    public int numberOfBooks;

    public BookStore(@NonNull String name, @NonNull String type, int numberOfBooks) {
        this.name = name;
        this.type = type;
        this.numberOfBooks = numberOfBooks;
    }

    @Override
    public String toString() {
        return "ID=" + id +
                " | Name=" + name +
                " | Type=" + type +
                " | Books=" + numberOfBooks;
    }
}