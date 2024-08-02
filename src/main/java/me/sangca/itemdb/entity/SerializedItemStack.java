package me.sangca.itemdb.entity;

public class SerializedItemStack {

    private Integer id;

    private String category;

    private String key;

    private String itemStackAsString;

    public SerializedItemStack() {}

    public SerializedItemStack(String category, String key, String itemStackAsString) {
        this.category = category;
        this.key = key;
        this.itemStackAsString = itemStackAsString;
    }

    public String getCategory() {
        return category;
    }

    public String getKey() {
        return key;
    }

    public String getItemStackAsString() {
        return itemStackAsString;
    }
}