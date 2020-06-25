package com.postman.costscalendar;

public class SubtypePiece {


    public SubtypePiece(int id, boolean is_sel) {
        this.id_view = id;
        this.is_selected = is_sel;

    }

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
    }

    public Integer getId_subtype() {
        return id_subtype;
    }

    public void setid_subtype(Integer id_type) {
        this.id_subtype = id_subtype;
    }

    public Boolean getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(Boolean is_selected) {
        this.is_selected = is_selected;
    }

    int id_view;
    String v_name;
    Integer id_subtype;
    Boolean is_selected;

}
