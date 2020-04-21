package com.example.butymovamoneytracker.screens.main.balance;

import androidx.lifecycle.LiveData;

import com.example.butymovamoneytracker.R;
import com.example.butymovamoneytracker.data.db.BalanceEntity;
import com.example.butymovamoneytracker.data.db.Database;
import com.example.butymovamoneytracker.states.FilterState;
import com.example.butymovamoneytracker.states.SearchState;
import com.example.butymovamoneytracker.utils.SingleLiveEvent;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BalanceFragmentViewModelImpl extends BalanceFragmentViewModel {

    private SingleLiveEvent<BalanceEntity> setBalanceEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> showErrorEvent = new SingleLiveEvent<>();

    private Database database;
    private FilterState filterState;
    private SearchState searchState;
    private CompositeDisposable compositeDisposable;

    private boolean isResumed;

    @Inject
    BalanceFragmentViewModelImpl(Database database,
                                 FilterState filterState,
                                 SearchState searchState,
                                 CompositeDisposable compositeDisposable) {
        this.database = database;
        this.filterState = filterState;
        this.searchState = searchState;
        this.compositeDisposable = compositeDisposable;

        filterState.addObserver((o, filterState1) -> {
            if (isResumed)
                getBalance();
        });

        searchState.addObserver((o, searchState1) -> {
            if (isResumed)
                getBalance();
        });
    }

    private void getBalance() {
        compositeDisposable.add( database.getBalance(filterState.getMinDate(),
                filterState.getMaxDate(),
                filterState.getFilterDate(),
                searchState.getSearchStr()
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(balanceEntity -> {
                    setBalanceEvent.setValue(balanceEntity);

                }, throwable -> showErrorEvent.setValue(R.string.balance_error)));
    }

    @Override
    void onResume() {
        getBalance();
        isResumed = true;
    }

    @Override
    void onStop() {
        isResumed = false;
    }

    @Override
    LiveData<BalanceEntity> setBalanceEvent() {
        return setBalanceEvent;
    }

    @Override
    LiveData<Integer> showErrorEvent() {
        return showErrorEvent;
    }

    @Override
    protected void onCleared() {
        filterState.deleteObservers();
        searchState.deleteObservers();

        compositeDisposable.dispose();
        super.onCleared();
    }
}
