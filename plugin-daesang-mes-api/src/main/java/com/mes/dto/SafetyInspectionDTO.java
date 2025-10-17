package com.mes.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 안전검사관리 request
public class SafetyInspectionDTO {
    private String uuid;

    // 기계기구명 MACHINE_NAME
    private String equipName;

    // 설치 장소 INSTALL_PLACE
    private String installLocation;

    // 형식 번호
    private String modelNo;

    // 자율안전확인신고 KCS
    private String kcsNo;

    // 규격 - 유체 SPEC_FLUID
    private String fluid;

    // 규격 - 용량 SPEC_CAPACITY
    private String capacity;

    // 단위
    private String valUnit;

    // 규격 - 설계압력 > 기준값 SPEC_DESIGN_PRESSURE
    private String planedValue;

    // 규격 - 운전압력 > 측정값 SPEC_OPERATING_PRESSURE
    private String currrentValue;

    // 제조사
    private String manufacturer;

    // 용도 USAGE
    private String purpose;

    // 유효기간(시작일자) VALIDITY_FROM
    private String startDate;

    // 유효기간(숫자)
    private String periodVal;

    // 유효기간(단위)
    private String periodUnit;

    // 비고 NOTE
    private String remark;

    public SafetyInspectionDTO(String UUID, String equipName, String installLocation, String modelNo, String kcsNo, String fluid, String capacity, String valUnit, String planedValue, String currrentValue, String manufacturer,
                               String purpose, String startDate, String periodVal, String periodUnit, String remark) {
        this.uuid = UUID;
        this.equipName = equipName;
        this.installLocation = installLocation;
        this.modelNo = modelNo;
        this.kcsNo = kcsNo;
        this.fluid = fluid;
        this.capacity = capacity;
        this.valUnit = valUnit;
        this.planedValue = planedValue;
        this.currrentValue = currrentValue;
        this.manufacturer = manufacturer;
        this.purpose = purpose;
        this.startDate = startDate;
        this.periodVal = periodVal;
        this.periodUnit = periodUnit;
        this.remark = remark;
    }

    public static List<SafetyInspectionDTO> toObjectArray(List<Map<String, Object>> arrays) {
        List<SafetyInspectionDTO> list = new ArrayList<>();
        for (Map<String, Object> map : arrays) {
            SafetyInspectionDTO req = new SafetyInspectionDTO (
                    String.valueOf(map.get("UUID")),
                    String.valueOf(map.get("MACHINE_NAME")),
                    String.valueOf(map.get("INSTALL_PLACE")),
                    String.valueOf(map.get("MODEL_NO")),
                    String.valueOf(map.get("KCS")),
                    String.valueOf(map.get("SPEC_FLUID")),
                    String.valueOf(map.get("SPEC_CAPACITY")),
                    "",
                    String.valueOf(map.get("SPEC_DESIGN_PRESSURE")),
                    String.valueOf(map.get("SPEC_OPERATING_PRESSURE")),
                    String.valueOf(map.get("MANUFACTURER")),
                    String.valueOf(map.get("USAGE")),
                    String.valueOf(map.get("VALIDITY_FROM")),
                    "",
                    "",
                    String.valueOf(map.get("NOTE"))
            );
            list.add(req);
        }
        return list;
    }

    // getter
    public String getUuid() {
        return uuid;
    }

    public String getEquipName() {
        return equipName;
    }

    public String getInstallLocation() {
        return installLocation;
    }

    public String getModelNo() {
        return modelNo;
    }

    public String getKcsNo() {
        return kcsNo;
    }

    public String getFluid() {
        return fluid;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getValUnit() {
        return valUnit;
    }

    public String getPlanedValue() {
        return planedValue;
    }

    public String getCurrrentValue() {
        return currrentValue;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getPeriodVal() {
        return periodVal;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public String getRemark() {
        return remark;
    }

    // setter
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public void setInstallLocation(String installLocation) {
        this.installLocation = installLocation;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public void setKcsNo(String kcsNo) {
        this.kcsNo = kcsNo;
    }

    public void setFluid(String fluid) {
        this.fluid = fluid;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setValUnit(String valUnit) {
        this.valUnit = valUnit;
    }

    public void setPlanedValue(String planedValue) {
        this.planedValue = planedValue;
    }

    public void setCurrrentValue(String currrentValue) {
        this.currrentValue = currrentValue;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setPeriodVal(String periodVal) {
        this.periodVal = periodVal;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
