package com.example.butymovamoneytracker.screens.addItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.butymovamoneytracker.data.model.Item;

public abstract class AddItemActivityViewModel extends ViewModel {

    abstract void addItem(Item item);

    abstract void btnAddEnable(String name, String price, String type);

    abstract LiveData<Boolean> btnAddEnabledEvent();

    abstract LiveData<Void> setFocusNameEvent();

    abstract LiveData<Void> setFocusPriceEvent();

    abstract LiveData<String> addItemCorrectEvent();

    abstract LiveData<String> addItemWrongEvent();
}
