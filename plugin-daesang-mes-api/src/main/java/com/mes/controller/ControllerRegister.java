package com.mes.controller;

import com.fr.decision.fun.impl.AbstractControllerRegisterProvider;
import com.fr.intelli.record.Focus;
import com.fr.record.analyzer.EnableMetrics;
import com.mes.service.ApiHelper;
import com.mes.service.MesApiService;

@EnableMetrics
public class ControllerRegister extends AbstractControllerRegisterProvider {

    @Override
    @Focus(id = "com.api.mes", text = "mes-api")
    public Class<?>[] getControllers() {
        return new Class[]{
                MESApiController.class, MESBatchController.class, MesApiService.class, ApiHelper.class
        };
    }
}
