package com.mes.dto;

// 안전검사관리 api 전달용 request
public class SafetyInspectionApiDTO {
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

    public SafetyInspectionApiDTO(String equipName, String installLocation, String modelNo, String kcsNo, String fluid, String capacity, String valUnit,
                                  String planedValue, String currrentValue, String manufacturer, String purpose, String startDate, String periodVal, String periodUnit, String remark) {
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


    public static SafetyInspectionApiDTO toObject(SafetyInspectionDTO dto) {
        return new SafetyInspectionApiDTO(
                dto.getEquipName(),
                dto.getInstallLocation(),
                dto.getModelNo(),
                dto.getKcsNo(),
                dto.getFluid(),
                dto.getCapacity(),
                dto.getValUnit(),
                dto.getPlanedValue(),
                dto.getCurrrentValue(),
                dto.getManufacturer(),
                dto.getPurpose(),
                dto.getStartDate(),
                dto.getPeriodVal(),
                dto.getPeriodUnit(),
                dto.getRemark()
        );
    }


    // getter
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
