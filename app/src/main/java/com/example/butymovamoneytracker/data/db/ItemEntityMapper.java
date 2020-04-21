package com.example.butymovamoneytracker.data.db;

import com.example.butymovamoneytracker.data.model.Item;
import com.example.butymovamoneytracker.utils.FormatUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ItemEntityMapper {

    private FormatUtils formatUtils;

    @Inject
    public ItemEntityMapper(FormatUtils formatUtils) {
        this.formatUtils = formatUtils;
    }

    public synchronized List<ItemEntity> mapItems(List<Item> items) {

        List<ItemEntity> itemEntities = new ArrayList<>();
        for(int i=0; i<items.size(); i++){
            itemEntities.add(mapItem(items.get(i)));
        }
        return itemEntities;
    }

    private ItemEntity mapItem(Item item) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(item.getId());
        itemEntity.setName(item.getName());
        itemEntity.setName_lower_case(item.getName().toLowerCase());
        itemEntity.setPrice(item.getPrice());
        itemEntity.setType(item.getType());
        try {
            itemEntity.setCreated_at(formatUtils.getStrToDateUTC(item.getCreated_at()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return itemEntity;
    }
}
