package com.example.butymovamoneytracker.screens.main.items;

import android.content.Intent;

import androidx.annotation.PluralsRes;
import androidx.lifecycle.LiveData;

import com.example.butymovamoneytracker.R;
import com.example.butymovamoneytracker.data.Api;
import com.example.butymovamoneytracker.data.db.Database;
import com.example.butymovamoneytracker.data.db.ItemEntity;
import com.example.butymovamoneytracker.data.db.ItemEntityMapper;
import com.example.butymovamoneytracker.data.extra.RemoveData;
import com.example.butymovamoneytracker.data.model.RemoveResult;
import com.example.butymovamoneytracker.prefs.Prefs;
import com.example.butymovamoneytracker.screens.main.adapters.TypePage;
import com.example.butymovamoneytracker.states.ActionModeState;
import com.example.butymovamoneytracker.states.FilterState;
import com.example.butymovamoneytracker.states.ItemsCountState;
import com.example.butymovamoneytracker.states.SearchState;
import com.example.butymovamoneytracker.states.SelectedItems;
import com.example.butymovamoneytracker.states.TimeZoneState;
import com.example.butymovamoneytracker.utils.SingleLiveEvent;

import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.example.butymovamoneytracker.dialogs.RemoveDialog.RC_REMOVE_DIALOG;
import static com.example.butymovamoneytracker.screens.addItem.AddItemActivity.EXTRA_NAME;
import static com.example.butymovamoneytracker.screens.addItem.AddItemActivity.RC_ADD_ITEM;

public class ItemsFragmentViewModelImpl extends ItemsFragmentViewModel {

    private SingleLiveEvent<List<ItemEntity>> itemEntityList = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> getItemsDoneEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> showAddItem = new SingleLiveEvent<>();
    private SingleLiveEvent<String> addItemResultTrueEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> addItemResultFalseEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<RemoveData> removeItemDialogShowEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<RemoveData> removeItemResultTrueEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> removeItemResultFalseEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> startActionModeEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> finishActionModeEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> setProgressEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> setRefreshEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Integer> refreshItemEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> refreshTitleEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> refreshItemsEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> scrollToPosition = new SingleLiveEvent<>();

    private SingleLiveEvent<Integer> showErrorEvent = new SingleLiveEvent<>();

    private Api api;
    private String typeItems;
    private Database database;
    private Prefs prefs;
    private ItemEntityMapper mapper;
    private SelectedItems selectedItems;
    private ActionModeState actionModeState;
    private FilterState filterState;
    private SearchState searchState;
    private ItemsCountState itemsCountState;
    private TimeZoneState timeZoneState;
    private CompositeDisposable compositeDisposable;
    private int nextValue;

    public ItemsFragmentViewModelImpl(Api api,
                                      Database database,
                                      ItemEntityMapper mapper,
                                      Prefs prefs,
                                      TypePage typePage,
                                      SelectedItems selectedItems,
                                      ActionModeState actionModeState,
                                      FilterState filterState,
                                      SearchState searchState,
                                      ItemsCountState itemsCountState,
                                      TimeZoneState timeZoneState,
                                      CompositeDisposable compositeDisposable) {
        this.api = api;
        this.database = database;
        this.mapper = mapper;
        this.prefs = prefs;
        this.typeItems = String.valueOf(typePage.getTypeValue());
        this.selectedItems = selectedItems;
        this.actionModeState = actionModeState;
        this.filterState = filterState;
        this.searchState = searchState;
        this.itemsCountState = itemsCountState;
        this.timeZoneState = timeZoneState;
        this.compositeDisposable = compositeDisposable;

        actionModeState.addObserver((o, actionModeState1) -> {
            if (!((ActionModeState) actionModeState1).isInActionMode() && typePage.equals(((ActionModeState) actionModeState1).getTypePage()))
                clearSelected();
        });

        filterState.addObserver((o, filterState1) -> getItemsDataBase());

        searchState.addObserver((o, searchState1) -> getItemsDataBase());

        timeZoneState.addObserver((observable, o) -> refreshItemsEvent.call());

        getItemsApi(false);
    }

    @Override
    void getItemsApi(Boolean isRefreshing){
        if (!isRefreshing)
            setProgressEvent.setValue(true);

        compositeDisposable.add(api.items(typeItems, prefs.getAuthToken())
                .map(items -> {
                    database.refreshItems(typeItems, mapper.mapItems(items));
                    return new Object();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    getItemsDoneEvent.setValue(true);
                    setProgressEvent.setValue(false);
                    setRefreshEvent.setValue(false);
                }, throwable -> {
                    setProgressEvent.setValue(false);
                    setRefreshEvent.setValue(false);
                    showErrorEvent.setValue(R.string.get_items_api_error);
                }));
    }

    @Override
    void getItemsDataBase() {
        compositeDisposable.add(database.getItems(typeItems,
                filterState.getMinDate(),
                filterState.getMaxDate(),
                filterState.getFilterDate(),
                searchState.getSearchStr())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemEntities -> {
                    itemEntityList.setValue(itemEntities);
                    itemsCountState.setCount(itemEntities.size());
                }, throwable -> showErrorEvent.setValue(R.string.get_items_database_error)
       ));
    }


    private void removeItems() {
        nextValue = 0;

        Observable.fromIterable(selectedItems.getItems().values())
                .subscribeOn(Schedulers.io())
                .flatMapSingle((Function<Long, SingleSource<RemoveResult>>) id -> api.remove(id, prefs.getAuthToken()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<RemoveResult>() {
                               @Override
                               public void onNext(RemoveResult removeResult) {
                                   if (removeResult != null && removeResult.getId()>0){
                                       nextValue++;
                                   }
                               }
                               @Override
                               public void onError(Throwable e) {
                                   if (nextValue>0) {
                                       removeItemResultTrueEvent.setValue(new RemoveData(nextValue, getPluralsRes()));
                                       getItemsApi(false);
                                   }
                                   else
                                       removeItemResultFalseEvent.call();
                               }

                               @Override
                               public void onComplete() {
                                   removeItemResultTrueEvent.setValue(new RemoveData(nextValue, getPluralsRes()));
                                   finishActionModeEvent.call();
                                   getItemsApi(false);
                               }
                           }
                );
    }

    @Override
    public void onClickItem(int position, long id) {
        if (!actionModeState.isInActionMode()) return;
        setSelectedItem(position, id);
    }

    @Override
    public void onLongClickItem(int position, long id) {
        if (!actionModeState.isInActionMode())
            startActionModeEvent.call();
        setSelectedItem(position, id);
    }

    @Override
    public void onMenuItemRemoveClick() {
        removeItemDialogShowEvent.setValue(new RemoveData(selectedItems.getItems().size(), getPluralsRes()));
    }

    @Override
    public void onFabClick() {
        showAddItem.call();
    }

    @Override
    void onRefresh() {
        getItemsApi(true);
    }

    @Override
    void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode){
            case RC_ADD_ITEM:{
                addItem(resultCode, intent);
                return;
            }
            case RC_REMOVE_DIALOG:{
                if (resultCode== RESULT_OK)
                    removeItems();
                return;
            }
        }
    }

    private @PluralsRes int getPluralsRes(){
        switch (Objects.requireNonNull(TypePage.getType(Integer.valueOf(typeItems)))){
            case PAGE_EXPENSE:
                return R.plurals.plurals_expense;
            case PAGE_INCOME:
                return R.plurals.plurals_income;
            default: throw new RuntimeException("Unknown type page");
        }
    }

    private void addItem(int resultCode, Intent intent){
        String name = intent != null ? intent.getStringExtra(EXTRA_NAME) : null;

        if (name == null)
            return;

        if (resultCode == RESULT_OK) {
            addItemResultTrueEvent.setValue(name);
            getItemsApi(false);
            scrollToPosition.setValue(0);
        }
        else
            addItemResultFalseEvent.setValue(name);
    }

    private void clearSelected(){
        selectedItems.getItems().clear();
        refreshItemsEvent.call();
    }

    private void setSelectedItem(int position, long id) {
        if (selectedItems.getItems().containsValue(id))
            selectedItems.getItems().remove(position);
        else
            selectedItems.getItems().put(position,id);

        refreshItemEvent.setValue(position);
        refreshTitleEvent.setValue(selectedItems.getItems().size());
    }

    @Override
    LiveData<Boolean> getItemsDoneEvent() {
        return getItemsDoneEvent;
    }

    @Override
    LiveData<List<ItemEntity>> itemEntityList() {
        return itemEntityList;
    }

    @Override
    LiveData<Void> showAddItem() {
        return showAddItem;
    }

    @Override
    LiveData<Void> startActionModeEvent() {
        return startActionModeEvent;
    }

    @Override
    LiveData<Void> finishActionModeEvent() {
        return finishActionModeEvent;
    }

    @Override
    LiveData<RemoveData> removeItemDialogShowEvent() {
        return removeItemDialogShowEvent;
    }

    @Override
    LiveData<RemoveData> removeItemResultTrueEvent() {
        return removeItemResultTrueEvent;
    }

    @Override
    LiveData<Void> removeItemResultFalseEvent() {
        return removeItemResultFalseEvent;
    }

    @Override
    LiveData<Boolean> setProgressEvent() {
        return setProgressEvent;
    }

    @Override
    LiveData<Boolean> setRefreshEvent() {
        return setRefreshEvent;
    }

    @Override
    LiveData<Integer> refreshItemEvent() {
        return refreshItemEvent;
    }

    @Override
    LiveData<Integer> showErrorEvent() {
        return showErrorEvent;
    }

    @Override
    LiveData<Void> refreshItemsEvent() {
        return refreshItemsEvent;
    }

    @Override
    LiveData<Integer> refreshTitleEvent() {
        return refreshTitleEvent;
    }

    @Override
    LiveData<String> addItemResultTrueEvent() {
        return addItemResultTrueEvent;
    }

    @Override
    LiveData<String> addItemResultFalseEvent() {
        return addItemResultFalseEvent;
    }

    @Override
    LiveData<Integer> scrollToPosition() {
        return scrollToPosition;
    }

    @Override
    protected void onCleared() {
        actionModeState.deleteObservers();
        searchState.deleteObservers();
        filterState.deleteObservers();
        timeZoneState.deleteObservers();

        compositeDisposable.dispose();
        super.onCleared();
    }
}
