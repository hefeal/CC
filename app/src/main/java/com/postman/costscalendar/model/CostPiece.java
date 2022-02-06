package com.postman.costscalendar.model;

public class CostPiece  {
    int viewId;
    boolean isSelected;

    public CostPiece (int id, boolean is_sel) {
        this.viewId = id;
        this.isSelected = is_sel;
        }
    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }



    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public void ChangeState()
    {
        if (isSelected)
            isSelected = false;
        else
            isSelected = true;
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
