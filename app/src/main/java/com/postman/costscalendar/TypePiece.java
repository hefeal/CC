package com.postman.costscalendar;

public class TypePiece {


    public TypePiece (int id, boolean is_sel) {
        this.id_view = id;
        this.is_selected = is_sel;

    }

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
    }

    public Integer getId_type() {
        return id_type;
    }

    public void setId_type(Integer id_type) {
        this.id_type = id_type;
    }

    public Boolean getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(Boolean is_selected) {
        this.is_selected = is_selected;
    }

    int id_view;
    String v_name;
    Integer id_type;
    Boolean is_selected;

}
