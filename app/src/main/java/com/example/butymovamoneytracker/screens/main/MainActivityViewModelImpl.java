package com.example.butymovamoneytracker.screens.main;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;

import com.example.butymovamoneytracker.R;
import com.example.butymovamoneytracker.data.Api;
import com.example.butymovamoneytracker.dialogs.DateDialog;
import com.example.butymovamoneytracker.prefs.FilterDate;
import com.example.butymovamoneytracker.prefs.Prefs;
import com.example.butymovamoneytracker.screens.main.adapters.TypePage;
import com.example.butymovamoneytracker.states.ActionModeState;
import com.example.butymovamoneytracker.states.FilterState;
import com.example.butymovamoneytracker.states.SearchState;
import com.example.butymovamoneytracker.states.SelectedItems;
import com.example.butymovamoneytracker.states.TimeZoneState;
import com.example.butymovamoneytracker.states.ToolbarState;
import com.example.butymovamoneytracker.utils.FormatUtils;
import com.example.butymovamoneytracker.utils.SingleLiveEvent;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;
import java.util.Observable;
import java.util.TimeZone;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static androidx.lifecycle.Lifecycle.State.RESUMED;

public class MainActivityViewModelImpl extends MainActivityViewModel  {

    private static final String KEY_CURRENT_TOOLBAR = "toolbar";
    private static final String KEY_PRIOR_TOOLBAR = "prior_toolbar";
    private static final String KEY_SELECTED_ITEMS = "selected_items";
    private static final String KEY_FILTER = "filter_date";
    private static final String KEY_SEARCH = "search_str";

    private static final String FORMAT_DATE = "dd.MM.yyyy";

    private SingleLiveEvent<Void> showLogOutDialogEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<FilterDate> showDateMinDialogEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<FilterDate> showDateMaxDialogEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<String> saveDateMinEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> saveDateMaxEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> showFABEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<ToolbarState> changeToolbarStateEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> onMenuItemRemoveClickEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<String> setSubtitleEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> setActionModeTitleEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> finishActionModeEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> showToastEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> finishTaskEvent = new SingleLiveEvent<>();

    private Api api;
    private Prefs prefs;
    private GoogleApiClient googleApiClient;
    private FormatUtils formatUtils;
    private ActionModeState actionModeState;
    private SelectedItems selectedItems;
    private FilterState filterState;
    private SearchState searchState;
    private ToolbarState toolbarState;
    private TimeZoneState timeZoneState;
    private CompositeDisposable compositeDisposable;

    private ToolbarState priorToolbarState;
    private TypePage currentTypePage;

    @Inject
    MainActivityViewModelImpl(Api api,
                              Prefs prefs,
                              GoogleApiClient googleApiClient,
                              FormatUtils formatUtils,
                              ActionModeState actionModeState,
                              SelectedItems selectedItems,
                              FilterState filterState,
                              SearchState searchState,
                              ToolbarState toolbarState,
                              TimeZoneState timeZoneState,
                              CompositeDisposable compositeDisposable) {
        this.api = api;
        this.prefs = prefs;
        this.googleApiClient = googleApiClient;
        this.formatUtils = formatUtils;
        this.actionModeState = actionModeState;
        this.selectedItems = selectedItems;
        this.filterState = filterState;
        this.searchState = searchState;
        this.toolbarState = toolbarState;
        this.timeZoneState = timeZoneState;
        this.compositeDisposable = compositeDisposable;

        actionModeState.addObserver((Observable o, Object actionModeState1) -> showFAB());

        timeZoneState.addObserver((observable, o) -> {
            setSubtitle();
            showToastEvent.setValue(R.string.timezone_change_toast);
        });
    }

    private void setSubtitle(){
        setSubtitleEvent.setValue(TimeZone.getDefault().getDisplayName());
    }

    private void showFAB() {
        showFABEvent.setValue(!actionModeState.isInActionMode() && currentTypePage != TypePage.PAGE_BALANCE);
    }

    @Override
    void init(Bundle savedInstanceState) {
        saveDateMinEvent.setValue(formatUtils.getDateStr(prefs.getFilterMinDate(), FORMAT_DATE));
        saveDateMaxEvent.setValue(formatUtils.getDateStr(prefs.getFilterMaxDate(), FORMAT_DATE));

        setSubtitle();

        currentTypePage = TypePage.PAGE_EXPENSE;

        if (savedInstanceState==null)
            return;

        if (savedInstanceState.containsKey(KEY_FILTER)) {
            Parcelable filterStateParcelable = savedInstanceState.getParcelable(KEY_FILTER);
            if (filterStateParcelable instanceof FilterState) {
                if (((FilterState) filterStateParcelable).getFilterDate()!=filterState.getFilterDate())
                    filterState.setFilterDate(((FilterState) filterStateParcelable).getFilterDate());
                filterState.setMinDate(((FilterState) filterStateParcelable).getMinDate());
                filterState.setMaxDate(((FilterState) filterStateParcelable).getMaxDate());
            }
        }

        if (savedInstanceState.containsKey(KEY_SEARCH) && savedInstanceState.getString(KEY_SEARCH)!=searchState.getSearchStr()){
            searchState.setSearchStr(savedInstanceState.getString(KEY_SEARCH));
        }

        if (savedInstanceState.containsKey(KEY_SELECTED_ITEMS)) {
            Serializable serializableItems = savedInstanceState.getSerializable(KEY_SELECTED_ITEMS);
            if (serializableItems instanceof SelectedItems) {
                selectedItems.setItems(((SelectedItems) serializableItems).getItems());
            }
        }
    }

    @Override
    void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_CURRENT_TOOLBAR, toolbarState==null ? ToolbarState.TOOLBAR : toolbarState);
        outState.putParcelable(KEY_PRIOR_TOOLBAR, priorToolbarState==null ? ToolbarState.TOOLBAR : priorToolbarState);
        outState.putSerializable(KEY_SELECTED_ITEMS, selectedItems);
        outState.putParcelable(KEY_FILTER, filterState);
        outState.putString(KEY_SEARCH, searchState.getSearchStr());
    }

    @Override
    void onRestoreInstanceState(Bundle savedInstanceState) {
        toolbarState = savedInstanceState.containsKey(KEY_CURRENT_TOOLBAR) ?
                (ToolbarState)savedInstanceState.getParcelable(KEY_CURRENT_TOOLBAR) :
                ToolbarState.TOOLBAR;

        changeToolbarStateEvent.setValue(toolbarState);

        priorToolbarState = savedInstanceState.containsKey(KEY_PRIOR_TOOLBAR) ?
                (ToolbarState)savedInstanceState.getParcelable(KEY_PRIOR_TOOLBAR) :
                ToolbarState.TOOLBAR;
    }

    private void logOut() {
        compositeDisposable.add( api.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(logoutResult -> {
                    if (logoutResult!=null) {
                        prefs.setAuthTokenEmpty();
                        Auth.GoogleSignInApi.signOut(googleApiClient);
                        showToastEvent.setValue(R.string.logout_result_correct);
                        finishTaskEvent.call();
                    }
                    else
                        showToastEvent.setValue(R.string.logout_result_wrong);
                }, throwable -> showToastEvent.setValue(R.string.logout_result_wrong)));
    }

    @Override
    public void logOutConfirmationYes() {
        logOut();
    }

    @Override
    void onDataSet(Object tag, int year, int month, int dayOfMonth) {
        if (tag.equals(DateDialog.DATE_MIN_DIALOG_TAG)){
            saveDateMin(new FilterDate(year, month, dayOfMonth));
        }
        else if (tag.equals(DateDialog.DATE_MAX_DIALOG_TAG)) {
            saveDateMax(new FilterDate(year, month, dayOfMonth));
        }
    }

    @Override
    public void onEtDateMinClick() {
        FilterDate filterDate = formatUtils.getFilterDate(prefs.getFilterMinDate(), TimeZone.getDefault());
        showDateMinDialogEvent.setValue(filterDate);
    }

    @Override
    public void onEtDateMaxClick() {
        FilterDate filterDate = formatUtils.getFilterDate(prefs.getFilterMaxDate(), TimeZone.getDefault());
        showDateMaxDialogEvent.setValue(filterDate);
    }

    private void saveDateMin(FilterDate filterDate) {
        long dateMin = formatUtils.getDateMinLong(filterDate);
        prefs.setFilterMinDate(dateMin);
        saveDateMinEvent.setValue(formatUtils.getDateStr(dateMin, FORMAT_DATE));
    }

    private void saveDateMax(FilterDate filterDate) {
        long dateMax = formatUtils.getDateMaxLong(filterDate);
        prefs.setFilterMaxDate(dateMax);
        saveDateMaxEvent.setValue(formatUtils.getDateStr(dateMax, FORMAT_DATE));
    }


    private void setActionMode(Boolean isInActionMode) {
        actionModeState.setInActionMode(isInActionMode);
    }

    @Override
    public void onMenuItemRemoveClick() {
        onMenuItemRemoveClickEvent.call();
    }

    @Override
    void onBtnFilterClick() {
        filterState.setMinDate(prefs.getFilterMinDate());
        filterState.setMaxDate(prefs.getFilterMaxDate());
        filterState.setFilterDate(true);
    }

    @Override
    void onBtnSearchClick(String searchStr) {
        doSearch(searchStr);
    }

    @Override
    void onMenuMainLogoutClick() {
        showLogOutDialogEvent.call();
    }

    @Override
    void onMenuMainFilterClick() {
        toolbarState = ToolbarState.FILTER_TOOLBAR;
        changeToolbarStateEvent.setValue(toolbarState);
    }

    @Override
    void onMenuMainSearchClick() {
        toolbarState = ToolbarState.SEARCH_TOOLBAR;
        changeToolbarStateEvent.setValue(toolbarState);
    }

    @Override
    void onMenuMainHomeClick() {
        if (filterState.getFilterDate())
            filterState.setFilterDate(false);

        if (!searchState.getSearchStr().equals(""))
            searchState.setSearchStr("");

        toolbarState = ToolbarState.TOOLBAR;
        changeToolbarStateEvent.setValue(toolbarState);
    }

    @Override
    void onPageChange(int position, Lifecycle.State state) {
        currentTypePage = TypePage.getType(position);

        if (actionModeState.isInActionMode() && state.isAtLeast(RESUMED))
            finishActionMode();

        showFAB();
    }

    @Override
    void onEditorActionSearchClick(String searchStr) {
        doSearch(searchStr);
    }

    private void doSearch(String searchStr){
        if (!searchStr.trim().equals(""))
            searchState.setSearchStr(searchStr.trim());
    }

    @Override
    void onBackPressed() {
        showLogOutDialogEvent.call();
    }

    @Override
    void startActionMode() {
        priorToolbarState = changeToolbarStateEvent.getValue();
        toolbarState = ToolbarState.ACTION_BAR;
        changeToolbarStateEvent.setValue(toolbarState);
    }

    @Override
    void finishActionMode() {
        finishActionModeEvent.call();
    }

    private void backPriorToobar() {
        if (priorToolbarState != null)
            toolbarState = priorToolbarState;
        else
            toolbarState = ToolbarState.TOOLBAR;
        changeToolbarStateEvent.setValue(toolbarState);
    }

    @Override
    void onCreatedActionMode() {
        setActionModeTitleEvent.setValue(selectedItems.getItems().size());
        actionModeState.setTypePage(currentTypePage);
        setActionMode(true);
    }

    @Override
    void onDestroyedActionMode() {
        backPriorToobar();
        setActionMode(false);
    }

    @Override
    LiveData<Void> showLogOutDialogEvent() {
        return showLogOutDialogEvent;
    }

    @Override
    LiveData<FilterDate> showDateMinDialogEvent() {
        return showDateMinDialogEvent;
    }

    @Override
    LiveData<FilterDate> showDateMaxDialogEvent() {
        return showDateMaxDialogEvent;
    }

    @Override
    LiveData<String> saveDateMinEvent() {
        return saveDateMinEvent;
    }

    @Override
    LiveData<String> saveDateMaxEvent() {
        return saveDateMaxEvent;
    }

    @Override
    LiveData<ToolbarState> changeToolbarStateEvent() {
        return changeToolbarStateEvent;
    }

    @Override
    LiveData<Boolean> showFABEvent() {
        return showFABEvent;
    }

    @Override
    LiveData<Void> onMenuItemRemoveClickEvent() {
        return onMenuItemRemoveClickEvent;
    }

    @Override
    LiveData<String> setSubtitleEvent() {
        return setSubtitleEvent;
    }

    @Override
    LiveData<Integer> setActionModeTitleEvent() {
        return setActionModeTitleEvent;
    }

    @Override
    LiveData<Void> finishActionModeEvent() {
        return finishActionModeEvent;
    }

    @Override
    LiveData<Integer> showToastEvent() {
        return showToastEvent;
    }

    @Override
    LiveData<Void> finishTaskEvent() {
        return finishTaskEvent;
    }

    @Override
    protected void onCleared() {
        actionModeState.deleteObservers();
        timeZoneState.deleteObservers();

        compositeDisposable.dispose();
        super.onCleared();
    }
}
