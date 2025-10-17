package com.mes.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 안전작업허가서 request
public class SafetyWorkPermitDTO {

    // 허가번호 ex) 순창-2025-07-01-2
    private String shePermitNo;

    // 작업 구분 ex) 일반
    private String sheWorkType;

    // 보충 작업 ex) 고소
    private String sheSupWork;

    // 작업 개요 ex) 현장 지정 합동 입회 및 케이블 보호, 상부 덮개 설치
    private String sheWorkSummary;

    // 작업 허가 기간 ex) 2025-07-01
    private String shePermitPeriod;

    // 발급(승일) 일자 ex) 2025-07-01
    private String sheApprovalDt;

    // 작업 완료 ex) Y / N
    private String sheWorkCompYn;

    public SafetyWorkPermitDTO(String no, String type, String work, String summary, String period, String approvalDt, String yn) {
        this.shePermitNo = no;
        this.sheWorkType = type;
        this.sheSupWork = work;
        this.sheWorkSummary = summary;
        this.shePermitPeriod = period;
        this.sheApprovalDt = approvalDt;
        this.sheWorkCompYn = yn;
    }

    public static List<SafetyWorkPermitDTO> toObjectArray(List<Map<String, Object>> arrays) {
        List<SafetyWorkPermitDTO> list = new ArrayList<>();
        for (Map<String, Object> map : arrays) {
            SafetyWorkPermitDTO req = new SafetyWorkPermitDTO(
                    String.valueOf(map.get("SHE_PERMIT_NO")),
                    String.valueOf(map.get("WORK_PERMIT_TYPE")),
                    String.valueOf(map.get("SHE_SUP_WORK")),
                    String.valueOf(map.get("WORK_SUMMARY")),
                    String.valueOf(map.get("WORK_PERMIT_YMD")),
                    String.valueOf(map.get("PERMIT_DT")),
                    String.valueOf(map.get("WORK_FINISH_WORKER_YN"))
            );

            list.add(req);
        }
        return list;
    }

    // getter
    public String getShePermitNo() {
        return shePermitNo;
    }

    public String getSheWorkType() {
        return sheWorkType;
    }

    public String getSheSupWork() {
        return sheSupWork;
    }

    public String getSheWorkSummary() {
        return sheWorkSummary;
    }

    public String getShePermitPeriod() {
        return shePermitPeriod;
    }

    public String getSheApprovalDt() {
        return sheApprovalDt;
    }

    public String getSheWorkCompYn() {
        return sheWorkCompYn;
    }

    // setter
    public void setShePermitNo(String shePermitNo) {
        this.shePermitNo = shePermitNo;
    }

    public void setSheWorkType(String sheWorkType) {
        this.sheWorkType = sheWorkType;
    }

    public void setSheSupWork(String sheSupWork) {
        this.sheSupWork = sheSupWork;
    }

    public void setSheWorkSummary(String sheWorkSummary) {
        this.sheWorkSummary = sheWorkSummary;
    }

    public void setShePermitPeriod(String shePermitPeriod) {
        this.shePermitPeriod = shePermitPeriod;
    }

    public void setSheApprovalDt(String sheApprovalDt) {
        this.sheApprovalDt = sheApprovalDt;
    }

    public void setSheWorkCompYn(String sheWorkCompYn) {
        this.sheWorkCompYn = sheWorkCompYn;
    }
}

