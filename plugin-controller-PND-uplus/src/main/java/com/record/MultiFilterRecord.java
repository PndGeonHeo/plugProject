package com.record;

import java.util.List;


public class MultiFilterRecord {
    private Integer seq;
    private String  userId;
    private String  page;
    private String  type;
    private String  colNm;
    private String  col;
    private String  col2;
    private String  col3;
    private String  col4;
    private String  col5;
    private String  defaultYn = "N";
    private String  regDt;
    private String  updDt;
    private String  mergeYn = "N";

    private List<MultiFilterRecord> cols;
    private List<MultiFilterDataRecord> values;

    public MultiFilterRecord() {}

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

    public List<MultiFilterRecord> getCols() {
        return cols;
    }

    public void setCols(List<MultiFilterRecord> cols) {
        this.cols = cols;
    }

    public List<MultiFilterDataRecord> getValues() {
        return values;
    }

    public void setValues(List<MultiFilterDataRecord> values) {
        this.values = values;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public String getDefaultYn() {
        return defaultYn;
    }

    public void setDefaultYn(String defaultYn) {
        this.defaultYn = defaultYn;
    }

    public String getColNm() {
        return colNm;
    }

    public void setColNm(String colNm) {
        this.colNm = colNm;
    }

    public String getCol3() {
        return col3;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    public String getCol5() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = col5;
    }

    public String getMergeYn() {
        return mergeYn;
    }

    public void setMergeYn(String mergeYn) {
        this.mergeYn = mergeYn;
    }
}
