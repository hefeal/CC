package com.postman.costscalendar;

public class TableTypesRow {


    public Integer getId_type() {
        return id_type;
    }

    public void setId_type(Integer id_type) {
        this.id_type = id_type;
    }

    private Integer id_type;

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
    }

    private String v_name;

    @Override
    public String toString() {
        return v_name;
    }
}
