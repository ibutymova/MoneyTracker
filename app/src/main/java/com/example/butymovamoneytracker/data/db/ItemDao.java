package com.example.butymovamoneytracker.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.Single;

@Dao
abstract class ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void saveItems(List<ItemEntity> itemEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void saveItem(ItemEntity itemEntity);

    @Query("delete from Item where type=:typeItems")
    abstract void deleteItems(String typeItems);

    @Transaction
    void refreshItems(String typeItems, List<ItemEntity> itemEntities){
        deleteItems(typeItems);
        saveItems(itemEntities);
    }

    @Query("delete from Item where id=:id")
    abstract int deleteItem(Long id);

    @Query(" select it.* from Item it where " +
            "  ((it.type = :type) and " +
            "   (not :filter or :date_min = 0 or it.created_at >= :date_min) and " +
            "   (not :filter or :date_max = 0 or it.created_at <= :date_max) and" +
            "   (it.name_lower_case like '%'||:search_str||'%' ))" +
            " order by created_at desc")
    abstract Single<List<ItemEntity>> getItems(String type, long date_min, long date_max, boolean filter, String search_str);

    @Query("SELECT " +
            "   coalesce(sum(case it.type when 0 then it.price else 0 end), 0) as total_expense, " +
            "   coalesce(sum(case it.type when 1 then it.price else 0 end), 0) as total_income, " +
            "   coalesce(sum(case it.type when 1 then it.price when 0 then - it.price else 0 end), 0) as balance " +
            "FROM item it " +
            "where " +
            "   ((not :filter or :date_min = 0 or it.created_at >= :date_min) and " +
            "    (not :filter or :date_max = 0 or it.created_at <= :date_max) and" +
            "    (it.name_lower_case like '%'||:search_str||'%' ))")
    abstract Single<BalanceEntity> getBalance(long date_min, long date_max, boolean filter, String search_str);

}
