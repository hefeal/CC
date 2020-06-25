package com.postman.costscalendar;

public class CostPiece  {
    int id_view;
    boolean is_selected;

    public CostPiece (int id, boolean is_sel) {
        this.id_view = id;
        this.is_selected = is_sel;
        }
    public int getId_view() {
        return id_view;
    }

    public void setId_view(int id_view) {
        this.id_view = id_view;
    }



    public boolean getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    public void ChangeState()
    {
        if (is_selected)
            is_selected = false;
        else
            is_selected = true;
    }

    public Integer getId_type() {
        return id_type;
    }

    public void setId_type(Integer id_type) {
        this.id_type = id_type;
    }

    Integer id_type;

    public Integer getId_subtype() {
        return id_subtype;
    }

    public void setId_subtype(Integer id_subtype) {
        this.id_subtype = id_subtype;
    }

    Integer id_subtype;

    public Float getF_sum() {
        return f_sum;
    }

    public void setF_sum(Float f_sum) {
        this.f_sum = f_sum;
    }

    Float f_sum;

    public String getV_comment() {
        return v_comment;
    }

    public void setV_comment(String v_comment) {
        this.v_comment = v_comment;
    }

    String v_comment;

}
