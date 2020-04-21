package com.example.butymovamoneytracker.states;

import java.util.Observable;

public class ItemsCountState extends Observable {

    private int count;

    public ItemsCountState(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        this.setChanged();
        this.notifyObservers(this);
    }
}
