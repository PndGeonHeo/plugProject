package com.mes.controller;

import com.fr.decision.webservice.annotation.LoginStatusChecker;
import com.fr.third.springframework.web.bind.annotation.GetMapping;
import com.fr.third.springframework.web.bind.annotation.RequestMapping;
import com.fr.third.springframework.web.bind.annotation.ResponseBody;
import com.fr.third.springframework.web.bind.annotation.RestController;
import com.mes.response.ApiResponse;
import com.mes.response.MesResponse;
import com.mes.service.MesApiService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 대상 MES Plugin Controller
 * sjang
 * 2025-10-14
 * <p>
 * 안전검사관리 등록 시 'XXX_FLAG = N' 인 항목을 조회하여 MES 시스템으로 전송하는 플러그인 컨트롤러
 * <p>
 * 흐름 요약:
 * 1. SAF_INSPECTION_LIST 테이블에서 '순창공장'의 미완료 항목 조회
 * 2. 조회 결과를 SafetyWorkPermitDTO DTO로 변환
 * 3. MES API (/ㅁㄴㅁㄴㅁ) 호출
 * 4. 전송 결과 로그 및 예외 처리
 * <p>
 * 관련 API:
 * - 안전검사관리 : POST /ㅁㅁㅁㅁㅁ
 * - 내부 쿼리: selectSafetyInspectionList (sqlmap.xml)
 */
@RestController
@RequestMapping(value = "/api")
@LoginStatusChecker(required = false)
public class MESApiController {

    @Autowired
    private MesApiService service;

    public MESApiController(MesApiService service) {
        this.service = service;
    }

    /**
     * 안전검사관리 API 호출
     */
    @ResponseBody
    @GetMapping(value = "/safety-inspections")
    public ApiResponse<MesResponse> safetyInspections() {
        return service.safetyInspectionsLogic();
    }
}
