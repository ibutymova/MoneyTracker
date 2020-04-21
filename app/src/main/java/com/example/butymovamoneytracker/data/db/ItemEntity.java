package com.example.butymovamoneytracker.data.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Item")
public class ItemEntity {
    @PrimaryKey
    private Long id;
    private String name;
    private String name_lower_case;
    private Long price;
    private String type;
    private Long created_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public String getName_lower_case() {
        return name_lower_case;
    }

    public void setName_lower_case(String name_lower_case) {
        this.name_lower_case = name_lower_case;
    }
}
