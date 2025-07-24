package com.record;

import java.util.List;

public class ParamFilterRecord {
    private String  userId;
    private String  page;
    private String  type;
    private String  col;
    private Integer sort;
    private String  value;
    private String  regDt;
    private String  updDt;
    private String  colUse = "Y";

    private List<ParamFilterRecord> dataList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public String getUpdDt() {
        return updDt;
    }

    public void setUpdDt(String updDt) {
        this.updDt = updDt;
    }

    public String getColUse() {
        return colUse;
    }

    public void setColUse(String colUse) {
        this.colUse = colUse;
    }

    public List<ParamFilterRecord> getDataList() {
        return dataList;
    }

    public void setDataList(List<ParamFilterRecord> dataList) {
        this.dataList = dataList;
    }
}
