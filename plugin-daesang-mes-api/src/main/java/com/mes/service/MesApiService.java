package com.mes.service;

import com.fr.cbb.platform.scaffold.dao.temp.spring.ObjectUtils;
import com.fr.third.springframework.stereotype.Service;
import com.mes.common.Common;
import com.mes.common.JdbcUtils;
import com.mes.dto.SafetyInspectionApiDTO;
import com.mes.dto.SafetyInspectionDTO;
import com.mes.dto.SafetyWorkPermitDTO;
import com.mes.response.ApiResponse;
import com.mes.response.MesResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mes.common.CommonJsonUtil.objectToString;

@Service
public class MesApiService extends ApiHelper {

    // 안전 검사 관리 등록 후 처리 로직
    public ApiResponse<MesResponse> safetyInspectionsLogic() {
        String resCode = "";
        try {
            List<SafetyInspectionDTO> list = findSafetyInspections();
            if (!ObjectUtils.isEmpty(list)) {
                List<SafetyInspectionApiDTO> dataList = list.stream()
                        .map(SafetyInspectionApiDTO::toObject)
                        .collect(Collectors.toList());

                String property = Common.getProperties("conf/app.properties").getProperty("api.mes.safety-inspection");

                MesResponse res = (MesResponse) callPostAPI(objectToString(dataList), property);
                resCode = res.getResult().getCode();

                if ("2000".equals(resCode)) {
                    // update 처리
                    updateSafetyInspections(list);
                } else {
                    // 실패일 경우 로그 찍기
                    log.error("Safety inspections api failed => {}, {}, {}, {}",
                            resCode, res.getResult().getStatus(), res.getResult().getType(), res.getResult().getDesc());
                    return new ApiResponse<>(false, resCode, res);
                }
            }
        } catch (Exception e) {
            log.error("Safety inspections failed => {}", e.getMessage());
        }
        return new ApiResponse<>(true, resCode);
    }

    // SAF_INSPECTION_LIST 테이블에 완료되지 않은 데이터 추출
    private List<SafetyInspectionDTO> findSafetyInspections() {
        List<SafetyInspectionDTO> list;
        try {
            JdbcUtils jdbcConnection = getJdbcConnection();
            List<Map<String, Object>> maps = jdbcConnection.queryForList("selectSafetyInspectionList");
            list = SafetyInspectionDTO.toObjectArray(maps);
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    // mes api 성공 후 처리
    private void updateSafetyInspections(List<SafetyInspectionDTO> list) {
        try {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("uuids", list.stream().map(SafetyInspectionDTO::getUuid).collect(Collectors.toList()));
            int result = getJdbcConnection().update("updateSafetyInspectionList", dataMap);

            log.info("Safety inspections update success count => {}", result);
        } catch (Exception e) {
            log.error("Safety inspections update failed => {}", e.getMessage());
        }
    }


    // --------------------------------------------------------------------
    /**
     * 안전작업허가서 중 작업 완료 N인 항목 MES API 호출
     */
    public void safetyWorkPermitLogic() {
        log.info("====START BATCH CONTROLLER====");
        try {
            // 작업 완료 N인 항목 보내기
            List<SafetyWorkPermitDTO> list = findIncompleteWorkPermits();

            if (!ObjectUtils.isEmpty(list)) {
                // 안전작업허가서
                MesResponse res = (MesResponse) callPostAPI(objectToString(list), props.getProperty("api.mes.safety-work-permit"));

                // 실패일 경우 로그 찍기
                if (!"2000".equals(res.getResult().getCode())) {
                    log.error("BATCH incomplete work permits api failed => {}, {}, {}, {}",
                            res.getResult().getCode(), res.getResult().getStatus(), res.getResult().getType(), res.getResult().getDesc());
                }
            }
        } catch (Exception e) {
            log.error("BATCH incomplete work permits failed => {}", e.getMessage());
        }

        log.info("====END BATCH CONTROLLER====");
    }


    // 완료되지 않은 안전작업허가서 조회
    private List<SafetyWorkPermitDTO> findIncompleteWorkPermits() {
        List<SafetyWorkPermitDTO> list;
        try {
            list = SafetyWorkPermitDTO.toObjectArray(getJdbcConnection().queryForList("selectIncompleteWorkPermitList"));
        } catch (Exception e) {
            return null;
        }
        return list;
    }
}
