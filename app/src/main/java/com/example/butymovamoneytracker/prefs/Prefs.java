package com.example.butymovamoneytracker.prefs;

public interface Prefs {

   void setAuthToken(String authToken);

   void setAuthTokenEmpty();

   String getAuthToken();

   boolean isSignIn();

   long getFilterMinDate();

   void setFilterMinDate(long minDate);

   long getFilterMaxDate();

   void setFilterMaxDate(long maxDate);

   String getLastTimeZone();

   void setLastTimeZone(String timeZone);
}
