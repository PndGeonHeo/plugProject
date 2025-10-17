package com.mes.response;

public class MesResponse {
    private Result result;

    public MesResponse() {
    }

    public MesResponse(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {

        // 응답 코드 2000, 5000, 4091
        private String code;

        // 응답 상태 “200”, “500”, “409”
        private String status;

        // 응답 타입 “IF-200”, “IF-500”, “IF-409”
        private String type;

        // 응답 메시지 “success”, “fail”, “not registered."
        private String desc;

        public Result() {
        }

        public Result(String code, String status, String type, String desc) {
            this.code = code;
            this.status = status;
            this.type = type;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getStatus() {
            return status;
        }

        public String getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
