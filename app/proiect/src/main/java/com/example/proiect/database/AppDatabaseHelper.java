package com.example.proiect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.proiect.models.PaperItem;
import com.example.proiect.models.ResearchThread;
import com.example.proiect.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "academic_engine.db";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_THREADS = "research_threads";
    public static final String TABLE_PAPERS = "papers";
    public static final String TABLE_FAVORITES = "favorites";
    public static final String TABLE_RATINGS = "ratings";
    public static final String TABLE_HISTORY = "history";

    // Common column names
    public static final String COL_ID = "id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_PAPER_ID = "paper_id";
    public static final String COL_THREAD_ID = "thread_id";

    // USERS Table - columns
    public static final String COL_USER_NAME = "name";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PASSWORD = "password";

    // THREADS Table - columns
    public static final String COL_THREAD_TITLE = "title";
    public static final String COL_THREAD_QUERY = "query";
    public static final String COL_THREAD_MODE = "mode";
    public static final String COL_THREAD_PLANNER = "planner";
    public static final String COL_THREAD_UPDATED = "updated_at";

    // PAPERS Table - columns
    public static final String COL_PAPER_TITLE = "title";
    public static final String COL_PAPER_AUTHORS = "authors";
    public static final String COL_PAPER_YEAR = "year";
    public static final String COL_PAPER_SOURCE = "source";
    public static final String COL_PAPER_DOI = "doi";
    public static final String COL_PAPER_URL = "url";
    public static final String COL_PAPER_ABSTRACT = "abstract";
    public static final String COL_PAPER_CITATIONS = "citation_count";
    public static final String COL_PAPER_INSTITUTION = "institution";
    public static final String COL_PAPER_COUNTRY = "country";
    public static final String COL_PAPER_LAT = "lat";
    public static final String COL_PAPER_LNG = "lng";
    public static final String COL_PAPER_OPENALEX = "openalex_id";

    // FAVORITES Table - columns
    public static final String COL_FAV_SAVED_AT = "saved_at";

    // RATINGS Table - columns
    public static final String COL_RATING_VALUE = "rating";

    // HISTORY Table - columns
    public static final String COL_HIST_OPENED_AT = "opened_at";

    // Table Create Statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_USER_NAME + " TEXT,"
            + COL_USER_EMAIL + " TEXT UNIQUE," + COL_USER_PASSWORD + " TEXT" + ")";

    private static final String CREATE_TABLE_THREADS = "CREATE TABLE " + TABLE_THREADS + "("
            + COL_THREAD_ID + " TEXT PRIMARY KEY," + COL_THREAD_TITLE + " TEXT,"
            + COL_THREAD_QUERY + " TEXT," + COL_THREAD_MODE + " TEXT,"
            + COL_THREAD_PLANNER + " TEXT," + COL_THREAD_UPDATED + " TEXT" + ")";

    private static final String CREATE_TABLE_PAPERS = "CREATE TABLE " + TABLE_PAPERS + "("
            + COL_ID + " TEXT PRIMARY KEY," + COL_THREAD_ID + " TEXT,"
            + COL_PAPER_TITLE + " TEXT," + COL_PAPER_AUTHORS + " TEXT,"
            + COL_PAPER_YEAR + " INTEGER," + COL_PAPER_SOURCE + " TEXT,"
            + COL_PAPER_DOI + " TEXT," + COL_PAPER_URL + " TEXT,"
            + COL_PAPER_ABSTRACT + " TEXT," + COL_PAPER_CITATIONS + " INTEGER,"
            + COL_PAPER_INSTITUTION + " TEXT," + COL_PAPER_COUNTRY + " TEXT,"
            + COL_PAPER_LAT + " REAL," + COL_PAPER_LNG + " REAL,"
            + COL_PAPER_OPENALEX + " TEXT" + ")";

    private static final String CREATE_TABLE_FAVORITES = "CREATE TABLE " + TABLE_FAVORITES + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_USER_ID + " INTEGER,"
            + COL_PAPER_ID + " TEXT," + COL_FAV_SAVED_AT + " TEXT" + ")";

    private static final String CREATE_TABLE_RATINGS = "CREATE TABLE " + TABLE_RATINGS + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_USER_ID + " INTEGER,"
            + COL_PAPER_ID + " TEXT," + COL_RATING_VALUE + " REAL" + ")";

    private static final String CREATE_TABLE_HISTORY = "CREATE TABLE " + TABLE_HISTORY + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_USER_ID + " INTEGER,"
            + COL_PAPER_ID + " TEXT," + COL_HIST_OPENED_AT + " TEXT" + ")";

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_THREADS);
        db.execSQL(CREATE_TABLE_PAPERS);
        db.execSQL(CREATE_TABLE_FAVORITES);
        db.execSQL(CREATE_TABLE_RATINGS);
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THREADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAPERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    // --- USER Methods ---
    public boolean insertUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public int verifyLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS, new String[]{COL_ID},
                    COL_USER_EMAIL + "=? AND " + COL_USER_PASSWORD + "=?",
                    new String[]{email, password}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return id;
    }

    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS, new String[]{COL_ID},
                    COL_USER_EMAIL + "=?", new String[]{email}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return id;
    }

    public User getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS, null, COL_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD)));
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return user;
    }

    public boolean updatePassword(int userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_PASSWORD, newPassword);
        int rows = db.update(TABLE_USERS, values, COL_ID + "=?", new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    // --- THREAD Methods ---
    public void insertOrUpdateThread(ResearchThread thread) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_THREAD_ID, thread.getThreadId());
        values.put(COL_THREAD_TITLE, thread.getTitle());
        values.put(COL_THREAD_QUERY, thread.getQuery());
        values.put(COL_THREAD_MODE, thread.getMode());
        values.put(COL_THREAD_PLANNER, thread.getPlanner());
        values.put(COL_THREAD_UPDATED, thread.getUpdatedAt());
        db.replace(TABLE_THREADS, null, values);
    }

    public List<ResearchThread> loadThreads() {
        List<ResearchThread> threads = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_THREADS, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ResearchThread thread = new ResearchThread();
                    thread.setThreadId(cursor.getString(cursor.getColumnIndexOrThrow(COL_THREAD_ID)));
                    thread.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_THREAD_TITLE)));
                    thread.setQuery(cursor.getString(cursor.getColumnIndexOrThrow(COL_THREAD_QUERY)));
                    thread.setMode(cursor.getString(cursor.getColumnIndexOrThrow(COL_THREAD_MODE)));
                    thread.setPlanner(cursor.getString(cursor.getColumnIndexOrThrow(COL_THREAD_PLANNER)));
                    thread.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COL_THREAD_UPDATED)));
                    
                    // Also populate paper count to avoid 0 count issue
                    thread.setPapers(loadPapersForThread(thread.getThreadId(), -1));
                    
                    threads.add(thread);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return threads;
    }

    // --- PAPER Methods ---
    public void insertOrUpdatePaper(PaperItem paper) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ID, paper.getId());
        values.put(COL_THREAD_ID, paper.getThreadId());
        values.put(COL_PAPER_TITLE, paper.getTitle());
        values.put(COL_PAPER_AUTHORS, paper.getAuthors());
        values.put(COL_PAPER_YEAR, paper.getYear());
        values.put(COL_PAPER_SOURCE, paper.getSource());
        values.put(COL_PAPER_DOI, paper.getDoi());
        values.put(COL_PAPER_URL, paper.getUrl());
        values.put(COL_PAPER_ABSTRACT, paper.getAbstractText());
        values.put(COL_PAPER_CITATIONS, paper.getCitationCount());
        values.put(COL_PAPER_INSTITUTION, paper.getInstitution());
        values.put(COL_PAPER_COUNTRY, paper.getCountry());
        values.put(COL_PAPER_LAT, paper.getLat());
        values.put(COL_PAPER_LNG, paper.getLng());
        values.put(COL_PAPER_OPENALEX, paper.getOpenAlexId());
        db.replace(TABLE_PAPERS, null, values);
    }

    public List<PaperItem> loadPapersForThread(String threadId, int userId) {
        List<PaperItem> papers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "SELECT p.*, r.rating FROM " + TABLE_PAPERS + " p LEFT JOIN " + TABLE_RATINGS + " r ON p.id = r.paper_id AND r.user_id = ? WHERE p.thread_id = ? ORDER BY p.year DESC";
            cursor = db.rawQuery(sql, new String[]{String.valueOf(userId), threadId});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    papers.add(extractPaperWithRating(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return papers;
    }

    public List<PaperItem> loadAllPapers(int userId) {
        List<PaperItem> papers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "SELECT p.*, r.rating FROM " + TABLE_PAPERS + " p LEFT JOIN " + TABLE_RATINGS + " r ON p.id = r.paper_id AND r.user_id = ?";
            cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    papers.add(extractPaperWithRating(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return papers;
    }

    public List<PaperItem> loadAllPapers() {
        return loadAllPapers(-1);
    }

    private PaperItem extractPaperWithRating(Cursor cursor) {
        PaperItem p = extractPaper(cursor);
        int ratingIdx = cursor.getColumnIndex("rating");
        if (ratingIdx != -1 && !cursor.isNull(ratingIdx)) {
            p.setUserRating(cursor.getFloat(ratingIdx));
        } else {
            p.setUserRating(0f);
        }
        return p;
    }

    public List<PaperItem> loadFavoritePapers(int userId) {
        List<PaperItem> papers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT p.*, r.rating FROM " + TABLE_PAPERS + " p " +
                "INNER JOIN " + TABLE_FAVORITES + " f ON p." + COL_ID + " = f." + COL_PAPER_ID + " " +
                "LEFT JOIN " + TABLE_RATINGS + " r ON p." + COL_ID + " = r." + COL_PAPER_ID + " AND r." + COL_USER_ID + " = ? " +
                "WHERE f." + COL_USER_ID + " = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(userId)});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    papers.add(extractPaperWithRating(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return papers;
    }

    public PaperItem loadPaperById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        PaperItem paper = null;
        try {
            cursor = db.query(TABLE_PAPERS, null, COL_ID + "=?", new String[]{id}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                paper = extractPaper(cursor);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return paper;
    }

    private PaperItem extractPaper(Cursor cursor) {
        PaperItem p = new PaperItem();
        p.setId(cursor.getString(cursor.getColumnIndexOrThrow(COL_ID)));
        p.setThreadId(cursor.getString(cursor.getColumnIndexOrThrow(COL_THREAD_ID)));
        p.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_PAPER_TITLE)));
        p.setAuthors(cursor.getString(cursor.getColumnIndexOrThrow(COL_PAPER_AUTHORS)));
        p.setYear(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAPER_YEAR)));
        p.setSource(cursor.getString(cursor.getColumnIndexOrThrow(COL_PAPER_SOURCE)));
        p.setDoi(cursor.getString(cursor.getColumnIndexOrThrow(COL_PAPER_DOI)));
        p.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(COL_PAPER_URL)));
        p.setAbstractText(cursor.getString(cursor.getColumnIndexOrThrow(COL_PAPER_ABSTRACT)));
        p.setCitationCount(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAPER_CITATIONS)));
        p.setInstitution(cursor.getString(cursor.getColumnIndexOrThrow(COL_PAPER_INSTITUTION)));
        p.setCountry(cursor.getString(cursor.getColumnIndexOrThrow(COL_PAPER_COUNTRY)));
        if (!cursor.isNull(cursor.getColumnIndexOrThrow(COL_PAPER_LAT))) {
            p.setLat(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PAPER_LAT)));
        }
        if (!cursor.isNull(cursor.getColumnIndexOrThrow(COL_PAPER_LNG))) {
            p.setLng(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PAPER_LNG)));
        }
        p.setOpenAlexId(cursor.getString(cursor.getColumnIndexOrThrow(COL_PAPER_OPENALEX)));
        return p;
    }

    // --- FAVORITES Methods ---
    public void addFavorite(int userId, String paperId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userId);
        values.put(COL_PAPER_ID, paperId);
        values.put(COL_FAV_SAVED_AT, String.valueOf(System.currentTimeMillis()));
        db.insert(TABLE_FAVORITES, null, values);
    }

    public void removeFavorite(int userId, String paperId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COL_USER_ID + "=? AND " + COL_PAPER_ID + "=?", new String[]{String.valueOf(userId), paperId});
    }

    public boolean isFavorite(int userId, String paperId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean exists = false;
        try {
            cursor = db.query(TABLE_FAVORITES, null, COL_USER_ID + "=? AND " + COL_PAPER_ID + "=?",
                    new String[]{String.valueOf(userId), paperId}, null, null, null);
            if (cursor != null) {
                exists = cursor.getCount() > 0;
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return exists;
    }

    // --- RATINGS Methods ---
    public void saveRating(int userId, String paperId, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userId);
        values.put(COL_PAPER_ID, paperId);
        values.put(COL_RATING_VALUE, rating);
        
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_RATINGS, null, COL_USER_ID + "=? AND " + COL_PAPER_ID + "=?",
                    new String[]{String.valueOf(userId), paperId}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                db.update(TABLE_RATINGS, values, COL_USER_ID + "=? AND " + COL_PAPER_ID + "=?", new String[]{String.valueOf(userId), paperId});
            } else {
                db.insert(TABLE_RATINGS, null, values);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public float loadRating(int userId, String paperId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        float rating = 0.0f;
        try {
            cursor = db.query(TABLE_RATINGS, new String[]{COL_RATING_VALUE}, COL_USER_ID + "=? AND " + COL_PAPER_ID + "=?",
                    new String[]{String.valueOf(userId), paperId}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                rating = cursor.getFloat(0);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return rating;
    }

    // --- HISTORY Methods ---
    public void addHistory(int userId, String paperId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userId);
        values.put(COL_PAPER_ID, paperId);
        values.put(COL_HIST_OPENED_AT, String.valueOf(System.currentTimeMillis()));
        db.insert(TABLE_HISTORY, null, values);
    }

    // --- ANALYTICS Methods ---
    public Map<String, Integer> getPapersBySource() {
        Map<String, Integer> data = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COL_PAPER_SOURCE + ", COUNT(*) FROM " + TABLE_PAPERS + " GROUP BY " + COL_PAPER_SOURCE, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String source = cursor.getString(0);
                    if (source == null || source.isEmpty()) source = "Unknown Source";
                    data.put(source, cursor.getInt(1));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return data;
    }

    public Map<Integer, Integer> getPapersByYear() {
        Map<Integer, Integer> data = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COL_PAPER_YEAR + ", COUNT(*) FROM " + TABLE_PAPERS + " GROUP BY " + COL_PAPER_YEAR + " ORDER BY " + COL_PAPER_YEAR, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    data.put(cursor.getInt(0), cursor.getInt(1));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return data;
    }

    public List<PaperItem> getTopPapersByCitations(int limit) {
        List<PaperItem> papers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_PAPERS, null, null, null, null, null, COL_PAPER_CITATIONS + " DESC", String.valueOf(limit));
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    papers.add(extractPaper(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return papers;
    }

    public int getRatedPapersCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_RATINGS + " WHERE " + COL_USER_ID + "=?", new String[]{String.valueOf(userId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public float getAverageRating(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(" + COL_RATING_VALUE + ") FROM " + TABLE_RATINGS + " WHERE " + COL_USER_ID + "=?", new String[]{String.valueOf(userId)});
        float avg = 0f;
        if (cursor.moveToFirst()) {
            avg = cursor.getFloat(0);
        }
        cursor.close();
        return avg;
    }
}
