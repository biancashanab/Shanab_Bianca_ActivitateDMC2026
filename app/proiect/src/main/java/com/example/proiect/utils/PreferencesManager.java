package com.example.proiect.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private static final String PREF_NAME = "AcademicEnginePrefs";

    private static final String KEY_USER_EMAIL = "logged_user_email";
    private static final String KEY_DEFAULT_MODE = "default_mode";
    private static final String KEY_DEFAULT_PLANNER = "default_planner";
    private static final String KEY_DEFAULT_SORT = "default_sort";
    private static final String KEY_AUTO_SAVE = "auto_save_results";
    private static final String KEY_RECENT_ONLY = "recent_papers_only";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_LAST_THREAD = "last_thread_id";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Email Utilizator
    public static void saveLoggedUserEmail(Context context, String email) {
        getPrefs(context).edit().putString(KEY_USER_EMAIL, email).apply();
    }
    public static String getLoggedUserEmail(Context context) {
        return getPrefs(context).getString(KEY_USER_EMAIL, "");
    }

    // Default Mode (research/discuss)
    public static void saveDefaultMode(Context context, String mode) {
        getPrefs(context).edit().putString(KEY_DEFAULT_MODE, mode).apply();
    }
    public static String getDefaultMode(Context context) {
        return getPrefs(context).getString(KEY_DEFAULT_MODE, "research");
    }

    // Default Planner
    public static void saveDefaultPlanner(Context context, String planner) {
        getPrefs(context).edit().putString(KEY_DEFAULT_PLANNER, planner).apply();
    }
    public static String getDefaultPlanner(Context context) {
        return getPrefs(context).getString(KEY_DEFAULT_PLANNER, "auto");
    }

    // Default Sort
    public static void saveDefaultSort(Context context, String sort) {
        getPrefs(context).edit().putString(KEY_DEFAULT_SORT, sort).apply();
    }
    public static String getDefaultSort(Context context) {
        return getPrefs(context).getString(KEY_DEFAULT_SORT, "relevance");
    }

    // Auto Save Results
    public static void saveAutoSaveResults(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_AUTO_SAVE, enabled).apply();
    }
    public static boolean isAutoSaveResults(Context context) {
        return getPrefs(context).getBoolean(KEY_AUTO_SAVE, true);
    }

    // Recent Papers Only
    public static void saveRecentPapersOnly(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_RECENT_ONLY, enabled).apply();
    }
    public static boolean isRecentPapersOnly(Context context) {
        return getPrefs(context).getBoolean(KEY_RECENT_ONLY, false);
    }

    // Dark Mode
    public static void saveDarkMode(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }
    public static boolean isDarkMode(Context context) {
        return getPrefs(context).getBoolean(KEY_DARK_MODE, false);
    }

    // Last Thread ID
    public static void saveLastThreadId(Context context, String threadId) {
        getPrefs(context).edit().putString(KEY_LAST_THREAD, threadId).apply();
    }
    public static String getLastThreadId(Context context) {
        return getPrefs(context).getString(KEY_LAST_THREAD, "");
    }

    public static void clearAll(Context context) {
        getPrefs(context).edit().clear().apply();
    }
}
