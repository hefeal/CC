package com.postman.costscalendar.model;

public class SubtypePiece {


    public SubtypePiece(int id, boolean is_sel) {
        this.viewId = id;
        this.isSelected = is_sel;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSubtypeId() {
        return subtypeId;
    }

    public void setid_subtype(Integer id_type) {
        this.subtypeId = subtypeId;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    int viewId;
    String name;
    Integer subtypeId;
    Boolean isSelected;

}
