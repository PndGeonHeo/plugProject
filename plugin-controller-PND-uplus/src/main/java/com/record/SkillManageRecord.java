package com.record;

import com.fr.third.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillManageRecord {

    private List<SkillManageRecord> dataList;
    private String cart;
    private String id;
    private String level;
    private String userId;
    private String  updDt;
    private List<SkillManageRecord> userList;
    private List<SkillManageRecord> skillList;
    private String skillCartId;
    private String operatorId;



    public void setDataList(List<SkillManageRecord> dataList) {
        this.dataList = dataList;
    }

    public List<SkillManageRecord> getDataList() {
        return dataList;
    }

    public String getCart() {
        return cart;
    }


    public void setCart(String cart) {
        this.cart = cart;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUpdDt() {
        return updDt;
    }

    public void setUpdDt(String updDt) {
        this.updDt = updDt;
    }


    public String getLevel() {
        return level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<SkillManageRecord> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<SkillManageRecord> skillList) {
        this.skillList = skillList;
    }

    public List<SkillManageRecord> getUserList() {
        return userList;
    }

    public void setUserList(List<SkillManageRecord> userList) {
        this.userList = userList;
    }

    public String getSkillCartId() {
        return skillCartId;
    }

    public void setSkillCartId(String skillCartId) {
        this.skillCartId = skillCartId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
}