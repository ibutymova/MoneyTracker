package com.example.butymovamoneytracker.screens.main.balance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.butymovamoneytracker.data.db.BalanceEntity;

abstract class BalanceFragmentViewModel extends ViewModel {

    abstract void onResume();

    abstract void onStop();

    abstract LiveData<BalanceEntity> setBalanceEvent();

    abstract LiveData<Integer> showErrorEvent();
}
