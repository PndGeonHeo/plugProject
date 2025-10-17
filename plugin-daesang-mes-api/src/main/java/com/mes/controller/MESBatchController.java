package com.mes.controller;

import com.fr.decision.webservice.annotation.LoginStatusChecker;
import com.fr.schedule.base.bean.output.OutputClass;
import com.fr.schedule.feature.output.OutputActionHandler;
import com.fr.third.springframework.web.bind.annotation.RestController;
import com.mes.service.MesApiService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 대상 MES BATCH Controller
 * sjang
 * 2025-10-14
 * <p>
 * '순창공장' 안전작업허가서 중 '작업 완료 여부 = N' 인 항목을 조회하여 MES 시스템으로 전송하는 배치 컨트롤러
 * <p>
 * 흐름 요약:
 * 1. SAF_WORK_PERMIT 테이블에서 미완료 항목 조회
 * 2. 조회 결과를 SafetyWorkPermitDTO DTO로 변환
 * 3. MES API (/exapp/v1/she/work-permit) 호출
 * 4. 전송 결과 로그 및 예외 처리
 * <p>
 * 관련 API:
 * - 안전작업허가서 : POST /exapp/v1/she/work-permit
 * - 내부 쿼리: selectIncompleteWorkPermitList (sqlmap.xml)
 */
@RestController
@LoginStatusChecker(required = false)
public class MESBatchController extends OutputActionHandler<OutputClass> {

    @Autowired
    private MesApiService service;

    public MESBatchController(MesApiService service) {
        this.service = service;
    }

    /**
     * 안전작업허가서 BATCH 실행
     */
    @Override
    public void doAction(OutputClass outputClass, Map<String, Object> map) {
        service.safetyWorkPermitLogic();
    }
}
