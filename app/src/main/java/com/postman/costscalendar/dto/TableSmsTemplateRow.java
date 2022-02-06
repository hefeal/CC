package com.postman.costscalendar.dto;

public class TableSmsTemplateRow {
    public Integer getSmsTempId() {
        return smsTempId;
    }

    public void setSmsTempId(Integer smsTempId) {
        this.smsTempId = smsTempId;
    }

    Integer smsTempId;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    String phoneNumber;

    public String getStringBeforeSum() {
        return stringBeforeSum;
    }

    public void setStringBeforeSum(String stringBeforeSum) {
        this.stringBeforeSum = stringBeforeSum;
    }

    String stringBeforeSum;

    public String getStringAfterSum() {
        return stringAfterSum;
    }

    public void setStringAfterSum(String stringAfterSum) {
        this.stringAfterSum = stringAfterSum;
    }

    String stringAfterSum;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    String templateName;
}
