package com.postman.costscalendar.dto;

public class TableTypesRow {

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    private Integer typeId;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    private String typeName;

    @Override
    public String toString() {
        return typeName;
    }
}
