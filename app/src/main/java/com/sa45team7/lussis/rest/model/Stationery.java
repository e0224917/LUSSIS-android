package com.sa45team7.lussis.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhatton on 1/17/18.
 */

public class Stationery {

    @SerializedName("ItemNum")
    public String itemNum;

    public String Category;

    public String Description;

    public int ReorderLevel;

    public int ReorderQty;

    public String UnitOfMeasure;

    public int CurrentQty;

    public String BinNum;

    public int AvailableQty;

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getReorderLevel() {
        return ReorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        ReorderLevel = reorderLevel;
    }

    public int getReorderQty() {
        return ReorderQty;
    }

    public void setReorderQty(int reorderQty) {
        ReorderQty = reorderQty;
    }

    public String getUnitOfMeasure() {
        return UnitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        UnitOfMeasure = unitOfMeasure;
    }

    public int getCurrentQty() {
        return CurrentQty;
    }

    public void setCurrentQty(int currentQty) {
        CurrentQty = currentQty;
    }

    public String getBinNum() {
        return BinNum;
    }

    public void setBinNum(String binNum) {
        BinNum = binNum;
    }

    public int getAvailableQty() {
        return AvailableQty;
    }

    public void setAvailableQty(int availableQty) {
        AvailableQty = availableQty;
    }
}
