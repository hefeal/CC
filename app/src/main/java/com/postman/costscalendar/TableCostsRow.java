package com.postman.costscalendar;

import java.util.Date;
import java.text.SimpleDateFormat;

public class TableCostsRow {
    public Integer getId_type() {
        return id_type;
    }

    public void setId_type(Integer id_type) {
        this.id_type = id_type;
    }

    private Integer id_type;

    public Integer getId_subtype() {
        return id_subtype;
    }

    public void setId_subtype(Integer id_subtype) {
        this.id_subtype = id_subtype;
    }

    private Integer id_subtype;

    public Integer getId_item() {
        return id_item;
    }

    public void setId_item(Integer id_item) {
        this.id_item = id_item;
    }

    private Integer id_item;

    public float getF_sum() {
        return f_sum;
    }

    public void setF_sum(float f_sum) {
        this.f_sum = f_sum;
    }

    private float f_sum;

    public String getV_comment() {
        return v_comment;
    }

    public void setV_comment(String v_comment) {
        this.v_comment = v_comment;
    }

    private String v_comment;

    public Date getDt_date() {
        return dt_date;
    }

    public String getDt_date_text()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(dt_date);
        return date;
    }

    public void setDt_date(Date dt_date) {
        this.dt_date = dt_date;
    }

    private Date dt_date;

    public void setId_cost(Integer id_cost) {
        this.id_cost = id_cost;
    }

    public Integer getId_cost() {
        return id_cost;
    }

    private Integer id_cost;

    public String getV_date() {
        return v_date;
    }

    public void setV_date(String v_date) {
        this.v_date = v_date;
    }

    private String v_date;

    public String getV_type_name() {
        return v_type_name;
    }

    public void setV_type_name(String v_type_name) {
        this.v_type_name = v_type_name;
    }

    private String v_type_name;

    public String getV_subtype_name() {
        return v_subtype_name;
    }

    public void setV_subtype_name(String v_subtype_name) {
        this.v_subtype_name = v_subtype_name;
    }

    private String v_subtype_name;

    public String getV_item_name() {
        return v_item_name;
    }

    public void setV_item_name(String v_item_name) {
        this.v_item_name = v_item_name;
    }

    private  String v_item_name;
}
