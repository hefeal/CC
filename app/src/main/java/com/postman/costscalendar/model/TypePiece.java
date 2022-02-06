package com.postman.costscalendar.model;

public class TypePiece {


    public TypePiece (int id, boolean is_sel) {
        this.viewId = id;
        this.isSelected = is_sel;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    int viewId;
    String name;
    Integer typeId;
    Boolean isSelected;

}
