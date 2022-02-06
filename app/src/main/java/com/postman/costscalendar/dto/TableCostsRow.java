package com.postman.costscalendar.dto;

import java.util.Date;
import java.text.SimpleDateFormat;

public class TableCostsRow {
    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    private Integer idType;

    public Integer getIdSubtype() {
        return idSubtype;
    }

    public void setIdSubtype(Integer idSubtype) {
        this.idSubtype = idSubtype;
    }

    private Integer idSubtype;

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    private Integer idItem;

    public float getCostSum() {
        return costSum;
    }

    public void setCostSum(float costSum) {
        this.costSum = costSum;
    }

    private float costSum;

    public String getCostComment() {
        return costComment;
    }

    public void setCostComment(String costComment) {
        this.costComment = costComment;
    }

    private String costComment;

    public Date getCostDate() {
        return costDate;
    }

    public String getDt_date_text()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(costDate);
        return date;
    }

    public void setCostDate(Date costDate) {
        this.costDate = costDate;
    }

    private Date costDate;

    public void setIdCost(Integer idCost) {
        this.idCost = idCost;
    }

    public Integer getIdCost() {
        return idCost;
    }

    private Integer idCost;

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    private String stringDate;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    private String typeName;

    public String getSubtypeName() {
        return subtypeName;
    }

    public void setSubtypeName(String subtypeName) {
        this.subtypeName = subtypeName;
    }

    private String subtypeName;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    private  String itemName;

    public Integer getSmsId() {
        return smsId;
    }

    public void setSmsId(Integer smsId) {
        this.smsId = smsId;
    }

    private Integer smsId;
}
