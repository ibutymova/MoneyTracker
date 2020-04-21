package com.example.butymovamoneytracker.screens.main.items;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.butymovamoneytracker.data.db.ItemEntity;
import com.example.butymovamoneytracker.data.extra.RemoveData;

import java.util.List;

public abstract class ItemsFragmentViewModel extends ViewModel {

    abstract void getItemsApi(Boolean isRefreshing);

    abstract void getItemsDataBase();

    public abstract void onClickItem(int position, long id);

    public abstract void onLongClickItem(int position, long id);

    public abstract void onMenuItemRemoveClick();

    public abstract void onFabClick();

    abstract void onRefresh();

    abstract void onActivityResult(int requestCode, int resultCode, Intent intent);

    abstract LiveData<Boolean> getItemsDoneEvent();

    abstract LiveData<List<ItemEntity>> itemEntityList();

    abstract LiveData<Void> showAddItem();

    abstract LiveData<Void> startActionModeEvent();

    abstract LiveData<Void> finishActionModeEvent();

    abstract LiveData<RemoveData> removeItemDialogShowEvent();

    abstract LiveData<RemoveData> removeItemResultTrueEvent();

    abstract LiveData<Void> removeItemResultFalseEvent();

    abstract LiveData<Boolean> setProgressEvent();

    abstract LiveData<Boolean> setRefreshEvent();

    abstract LiveData<Integer> showErrorEvent();

    abstract LiveData<Void> refreshItemsEvent();

    abstract LiveData<Integer> refreshItemEvent();

    abstract LiveData<Integer> refreshTitleEvent();

    abstract LiveData<String> addItemResultTrueEvent();

    abstract LiveData<String> addItemResultFalseEvent();

    abstract LiveData<Integer> scrollToPosition();
}
