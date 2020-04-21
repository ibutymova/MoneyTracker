package com.example.butymovamoneytracker.states;

import java.io.Serializable;
import java.util.Map;
import java.util.Observable;

public class SelectedItems extends Observable implements Serializable {

    private Map<Integer, Long> items;

    public SelectedItems(Map<Integer, Long> items) {
        this.items = items;
    }

    public Map<Integer, Long> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Long> items) {
        this.items = items;
        this.setChanged();
        this.notifyObservers(this);
    }
}
