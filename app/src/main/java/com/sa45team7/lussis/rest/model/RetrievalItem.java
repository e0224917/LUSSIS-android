package com.sa45team7.lussis.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhatton on 1/23/18.
 */

public class RetrievalItem {

    @SerializedName("ItemNum")
    private String itemNum;

    @SerializedName("BinNum")
    private String binNum;

    @SerializedName("Description")
    private String description;

    @SerializedName("UnitOfMeasure")
    private String unitOfMeasure;

    //In stock
    @SerializedName("AvailableQty")
    private int availableQty;

    @SerializedName("RequestedQty")
    private int requestedQty;

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public String getBinNum() {
        return binNum;
    }

    public void setBinNum(String binNum) {
        this.binNum = binNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(int availableQty) {
        this.availableQty = availableQty;
    }

    public int getRequestedQty() {
        return requestedQty;
    }

    public void setRequestedQty(int requestedQty) {
        this.requestedQty = requestedQty;
    }
}
