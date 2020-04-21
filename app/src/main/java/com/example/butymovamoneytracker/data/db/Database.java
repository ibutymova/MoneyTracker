package com.example.butymovamoneytracker.data.db;

import java.util.List;

import io.reactivex.Single;

public interface Database {

    void saveItems(List<ItemEntity> itemEntities);

    void saveItem(ItemEntity itemEntity);

    void deleteItems(String typeItems);

    void refreshItems(String typeItems, List<ItemEntity> itemEntities);

    void deleteItem(Long id);

    Single<List<ItemEntity>> getItems(String type, long date_min, long date_max, boolean filter, String search_str);

    Single<BalanceEntity> getBalance(long date_min, long date_max, boolean filter, String search_str);


}
