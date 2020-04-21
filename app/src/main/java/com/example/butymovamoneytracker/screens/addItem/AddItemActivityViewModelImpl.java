package com.example.butymovamoneytracker.screens.addItem;

import androidx.lifecycle.LiveData;

import com.example.butymovamoneytracker.data.Api;
import com.example.butymovamoneytracker.data.model.Item;
import com.example.butymovamoneytracker.prefs.Prefs;
import com.example.butymovamoneytracker.utils.SingleLiveEvent;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddItemActivityViewModelImpl extends AddItemActivityViewModel {

    private SingleLiveEvent<Boolean> btnAddEnabledEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> addItemCorrectEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> addItemWrongEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> setFocusNameEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> setFocusPriceEvent = new SingleLiveEvent<>();

    private Api api;
    private Prefs prefs;
    private CompositeDisposable compositeDisposable;

    @Inject
    AddItemActivityViewModelImpl(Api api,
                                 Prefs prefs,
                                 CompositeDisposable compositeDisposable) {
        this.api = api;
        this.prefs = prefs;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    void addItem(Item item) {
        compositeDisposable.add( api.add(item, prefs.getAuthToken())
                    .subscribeOn(Schedulers.io())
                    .subscribe(addResult -> {
                        if (addResult.getId() > 0)
                            addItemCorrectEvent.postValue(item.getName());
                        else
                            addItemWrongEvent.postValue(item.getName());
                    },
                            throwable -> addItemWrongEvent.postValue(item.getName()))
        );
    }

    @Override
    void btnAddEnable(String name, String price, String type) {
        if ((name.length() > 0)&&(price.length() > 0)){
            addItem(new Item(name, Long.valueOf(price), type));
            btnAddEnabledEvent.setValue(false);
        }
        else
            if(name.length() > 0){
                setFocusPriceEvent.call();
            }
            else
                if (price.length() > 0) {
                    setFocusNameEvent.call();
                }
    }

    @Override
    LiveData<Boolean> btnAddEnabledEvent() {
        return btnAddEnabledEvent;
    }

    @Override
    LiveData<Void> setFocusNameEvent() {
        return setFocusNameEvent;
    }

    @Override
    LiveData<Void> setFocusPriceEvent() {
        return setFocusPriceEvent;
    }

    @Override
    LiveData<String> addItemCorrectEvent() {
        return addItemCorrectEvent;
    }

    @Override
    LiveData<String> addItemWrongEvent() {
        return addItemWrongEvent;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
