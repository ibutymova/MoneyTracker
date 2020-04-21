package com.example.butymovamoneytracker.states;

import java.util.Observable;

public class TimeZoneState extends Observable {

    private String timeZoneId;

    public TimeZoneState(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
        this.setChanged();
        this.notifyObservers();
    }
}
