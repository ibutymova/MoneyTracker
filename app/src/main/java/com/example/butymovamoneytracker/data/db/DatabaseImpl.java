package com.example.butymovamoneytracker.data.db;

import java.util.List;

import io.reactivex.Single;

public class DatabaseImpl implements Database {
    private AppDatabase database;

    DatabaseImpl(AppDatabase database) {
        this.database = database;
    }

    @Override
    public void saveItems(List<ItemEntity> itemEntities) {
       database.itemDao().saveItems(itemEntities);
    }

    @Override
    public void saveItem(ItemEntity itemEntity) {
        database.itemDao().saveItem(itemEntity);
    }

    @Override
    public void deleteItems(String typeItems) {
        database.itemDao().deleteItems(typeItems);
    }

    @Override
    public void refreshItems(String typeItems, List<ItemEntity> itemEntities) {
        database.itemDao().refreshItems(typeItems, itemEntities);
    }

    @Override
    public void deleteItem(Long id) {
        database.itemDao().deleteItem(id);
    }

    @Override
    public Single<BalanceEntity> getBalance(long date_min, long date_max, boolean filter, String search_str) {
        return database.itemDao().getBalance(date_min, date_max, filter, search_str);
    }

    @Override
    public Single<List<ItemEntity>> getItems(String type, long date_min, long date_max, boolean filter, String search_str) {
        return database.itemDao().getItems(type, date_min, date_max, filter, search_str);
    }
}
