package com.example.lab4;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookStore implements Serializable {

    public enum StoreType {
        ONLINE,
        PHYSICAL,
        HYBRID
    }

    private final String name;
    private final int numberOfBooks;
    private final boolean open24h;
    private final double averagePrice;
    private final StoreType storeType;
    private final Date openingDate;

    public BookStore(String name, int numberOfBooks, boolean open24h, StoreType storeType, double averagePrice, Date openingDate) {
        this.name = name;
        this.numberOfBooks = numberOfBooks;
        this.open24h = open24h;
        this.storeType = storeType;
        this.averagePrice = averagePrice;
        this.openingDate = openingDate;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfBooks() {
        return numberOfBooks;
    }

    public boolean isOpen24h() {
        return open24h;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public StoreType getStoreType() {
        return storeType;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return name + " | " + numberOfBooks + " carti | " + (open24h ? "Open 24h" : "Program normal") + " | " + storeType + " | " +
                averagePrice + " lei | " + sdf.format(openingDate);
    }
}