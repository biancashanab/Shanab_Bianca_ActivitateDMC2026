package com.example.lab4;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookStore implements Parcelable, Serializable {

    public enum StoreType {
        ONLINE,
        PHYSICAL,
        HYBRID
    }

    private String name;
    private int numberOfBooks;
    private boolean open24h;
    private double averagePrice;
    private StoreType storeType;
    private Date openingDate;

    public BookStore(String name, int numberOfBooks, boolean open24h,
                     StoreType storeType, double averagePrice, Date openingDate) {
        this.name = name;
        this.numberOfBooks = numberOfBooks;
        this.open24h = open24h;
        this.storeType = storeType;
        this.averagePrice = averagePrice;
        this.openingDate = openingDate;
    }

    protected BookStore(Parcel in) {
        name = in.readString();
        numberOfBooks = in.readInt();
        open24h = in.readByte() != 0;
        averagePrice = in.readDouble();
        storeType = StoreType.valueOf(in.readString());
        openingDate = new Date(in.readLong());
    }

    public static final Creator<BookStore> CREATOR = new Creator<BookStore>() {
        @Override
        public BookStore createFromParcel(Parcel in) {
            return new BookStore(in);
        }

        @Override
        public BookStore[] newArray(int size) {
            return new BookStore[size];
        }
    };

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

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfBooks(int numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }

    public void setOpen24h(boolean open24h) {
        this.open24h = open24h;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(numberOfBooks);
        dest.writeByte((byte) (open24h ? 1 : 0));
        dest.writeDouble(averagePrice);
        dest.writeString(storeType.name());
        dest.writeLong(openingDate.getTime());
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return name + " | " + numberOfBooks + " books | " +
                (open24h ? "Open 24h" : "Normal") + " | " +
                storeType + " | " + averagePrice + " | " +
                sdf.format(openingDate);
    }
}