package com.example.lab4;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileHelper {

    public static final String ALL_BOOKSTORES_FILE = "bookstores.dat";
    public static final String FAVORITES_FILE = "favorites.dat";

    public static void appendBookStoreToFile(Context context, String fileName, BookStore store) {
        ArrayList<BookStore> list = readBookStoresFromFile(context, fileName);
        list.add(store);
        writeBookStoresToFile(context, fileName, list);
    }

    public static void writeBookStoresToFile(Context context, String fileName, ArrayList<BookStore> list) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<BookStore> readBookStoresFromFile(Context context, String fileName) {
        ArrayList<BookStore> list = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<BookStore>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            // fisierul poate sa nu existe prima data
        }

        return list;
    }
}