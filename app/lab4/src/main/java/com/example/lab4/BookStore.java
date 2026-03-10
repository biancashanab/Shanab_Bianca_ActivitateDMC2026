package com.example.lab4;

public class BookStore {

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


    public BookStore(String name, int numberOfBooks, boolean open24h, StoreType storeType, double averagePrice) {
        this.name = name;
        this.numberOfBooks = numberOfBooks;
        this.open24h = open24h;
        this.storeType = storeType;
        this.averagePrice = averagePrice;
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

    public StoreType getStoreType() {
        return storeType;
    }

    public double getAveragePrice() {
        return averagePrice;
    }
}