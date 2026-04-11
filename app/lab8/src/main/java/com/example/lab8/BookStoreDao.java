package com.example.lab8;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookStoreDao {

    // 1. Inserare obiect
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(BookStore bookStore);

    // 2. Selectie toate inregistrarile
    @Query("SELECT * FROM bookstores ORDER BY id ASC")
    List<BookStore> getAll();

    // 3. Selectie obiect cu valoare string egala cu parametrul
    @Query("SELECT * FROM bookstores WHERE name = :value LIMIT 1")
    BookStore getByName(String value);

    // 4. Selectie obiecte cu valoare intreaga intr-un interval
    @Query("SELECT * FROM bookstores WHERE numberOfBooks BETWEEN :minValue AND :maxValue ORDER BY numberOfBooks ASC")
    List<BookStore> getByBooksInterval(int minValue, int maxValue);

    // 5a. Stergere inregistrari cu valoare numerica mai mare decat parametrul
    @Query("DELETE FROM bookstores WHERE numberOfBooks > :value")
    int deleteWhereBooksGreaterThan(int value);

    // 5b. Stergere inregistrari cu valoare numerica mai mica decat parametrul
    @Query("DELETE FROM bookstores WHERE numberOfBooks < :value")
    int deleteWhereBooksLessThan(int value);

    // 6. Crestere cu o unitate a valorii numerice
    // pentru toate inregistrarile al caror nume incepe cu litera primita
    @Query("UPDATE bookstores SET numberOfBooks = numberOfBooks + 1 WHERE name LIKE :letter || '%'")
    int incrementBooksForNamesStartingWith(String letter);
}