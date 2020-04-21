package com.example.butymovamoneytracker.screens.main;

import android.os.Bundle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.butymovamoneytracker.prefs.FilterDate;
import com.example.butymovamoneytracker.states.ToolbarState;

public abstract class MainActivityViewModel extends ViewModel {

    abstract void init(Bundle savedInstanceState);

    abstract void onSaveInstanceState(Bundle outState);

    abstract void onRestoreInstanceState(Bundle savedInstanceState);

    public abstract void logOutConfirmationYes();

    abstract void onDataSet(Object tag, int year, int month, int dayOfMonth);

    public abstract void onEtDateMinClick();

    public abstract void onEtDateMaxClick();

    public abstract void onMenuItemRemoveClick();

    abstract void onBtnFilterClick();

    abstract void onBtnSearchClick(String searchStr);

    abstract void onMenuMainLogoutClick();

    abstract void onMenuMainFilterClick();

    abstract void onMenuMainSearchClick();

    abstract void onMenuMainHomeClick();

    abstract void onPageChange(int position, Lifecycle.State state);

    abstract void onEditorActionSearchClick(String searchStr);

    abstract void onBackPressed();

    abstract void startActionMode();

    abstract void finishActionMode();

    abstract void onCreatedActionMode();

    abstract void onDestroyedActionMode();

    abstract LiveData<Void> showLogOutDialogEvent();

    abstract LiveData<FilterDate> showDateMinDialogEvent();

    abstract LiveData<FilterDate> showDateMaxDialogEvent();

    abstract LiveData<String> saveDateMinEvent();

    abstract LiveData<String> saveDateMaxEvent();

    abstract LiveData<ToolbarState> changeToolbarStateEvent();

    abstract LiveData<Boolean> showFABEvent();

    abstract LiveData<Void> onMenuItemRemoveClickEvent();

    abstract LiveData<String> setSubtitleEvent();

    abstract LiveData<Integer> setActionModeTitleEvent();

    abstract LiveData<Void> finishActionModeEvent();

    abstract LiveData<Integer> showToastEvent();

    abstract LiveData<Void> finishTaskEvent();
}
