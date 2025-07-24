package com.controller;

import com.common.Common;
import com.fr.decision.fun.impl.AbstractControllerRegisterProvider;
import com.fr.intelli.record.Focus;
import com.fr.record.analyzer.EnableMetrics;

@EnableMetrics
public class ControllerRegister extends AbstractControllerRegisterProvider {

    @Override
    @Focus(id="com.api.dev", text = "api-dev")
    public Class<?>[] getControllers() {
        return new Class[]{
                Param.class,ftp.class, Common.class, CommLogin.class

        };
    }
}
