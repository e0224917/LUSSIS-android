package com.sa45team7.lussis.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhatton on 1/20/18.
 */

class RequisitionDetail {

    @SerializedName("Description")
    private String description;

    @SerializedName("UnitOfMeasure")
    private String unitOfMeasure;

    @SerializedName("Quantity")
    private int quantity;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
