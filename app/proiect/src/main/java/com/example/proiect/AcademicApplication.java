package com.example.proiect;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.proiect.utils.PreferencesManager;

public class AcademicApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Apply theme globally on startup
        if (PreferencesManager.isDarkMode(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
