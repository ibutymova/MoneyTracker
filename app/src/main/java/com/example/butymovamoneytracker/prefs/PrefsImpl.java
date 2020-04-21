package com.example.butymovamoneytracker.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.butymovamoneytracker.utils.FormatUtils;

import java.util.TimeZone;

public class PrefsImpl implements Prefs {

    private static final String PREFERENCES_SESSION = "session";
    private static final String KEY_AUTH_TOKEN = "auth-token";
    private static final String KEY_FILTER_MIN_DATE = "min-date";
    private static final String KEY_FILTER_MAX_DATE = "max-date";
    private static final String KEY_LAST_TIMEZONE = "timezone";

    private Context context;
    private FormatUtils formatUtils;

    public PrefsImpl(Context context, FormatUtils formatUtils) {
        this.context = context;
        this.formatUtils = formatUtils;
    }

    private SharedPreferences getSharedPreferences(){
        return context.getSharedPreferences(PREFERENCES_SESSION, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditorSharedPreferences(){
       return getSharedPreferences().edit();
    }

    @Override
    public void setAuthToken(String authToken) {
        getEditorSharedPreferences().putString(KEY_AUTH_TOKEN, authToken).apply();
    }

    @Override
    public void setAuthTokenEmpty() {
        getEditorSharedPreferences().putString(KEY_AUTH_TOKEN, "").apply();
    }

    @Override
    public String getAuthToken() {
       return getSharedPreferences().getString(KEY_AUTH_TOKEN, "");
    }

    @Override
    public boolean isSignIn() {
        return TextUtils.isEmpty(getAuthToken());
    }

    @Override
    public long getFilterMinDate() {
        FilterDate filterDate = formatUtils.getFilterDate(0, TimeZone.getDefault());
        return getSharedPreferences().getLong(KEY_FILTER_MIN_DATE, formatUtils.getDateMinLong(filterDate));
    }

    @Override
    public void setFilterMinDate(long minDate) {
        getEditorSharedPreferences().putLong(KEY_FILTER_MIN_DATE, minDate).apply();
    }

    @Override
    public long getFilterMaxDate() {
        FilterDate filterDate = formatUtils.getFilterDate(0, TimeZone.getDefault());
        return getSharedPreferences().getLong(KEY_FILTER_MAX_DATE, formatUtils.getDateMaxLong(filterDate));
    }

    @Override
    public void setFilterMaxDate(long maxDate) {
        getEditorSharedPreferences().putLong(KEY_FILTER_MAX_DATE, maxDate).apply();
    }

    @Override
    public String getLastTimeZone() {
        return getSharedPreferences().getString(KEY_LAST_TIMEZONE, null);
    }

    @Override
    public void setLastTimeZone(String timeZone) {
        getEditorSharedPreferences().putString(KEY_LAST_TIMEZONE, timeZone).apply();
    }
}
