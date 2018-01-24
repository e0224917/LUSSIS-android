package com.sa45team7.lussis.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhatton on 1/24/18.
 */

public class Adjustment {

    @SerializedName("ItemNum")
    private String itemNum;

    @SerializedName("Quantity")
    private int quantity;

    @SerializedName("Remark")
    private String reason;

    @SerializedName("RequestEmpNum")
    private int requestEmpNum;

    @SerializedName("ApprovalEmpnum")
    private int approvalEmpnum;

    @SerializedName("Remark")
    private String remark;

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getRequestEmpNum() {
        return requestEmpNum;
    }

    public void setRequestEmpNum(int requestEmpNum) {
        this.requestEmpNum = requestEmpNum;
    }

    public int getApprovalEmpnum() {
        return approvalEmpnum;
    }

    public void setApprovalEmpnum(int approvalEmpnum) {
        this.approvalEmpnum = approvalEmpnum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}