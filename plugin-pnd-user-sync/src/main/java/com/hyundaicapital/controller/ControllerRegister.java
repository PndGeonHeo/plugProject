package com.hyundaicapital.controller;
import com.fr.decision.fun.impl.AbstractControllerRegisterProvider;
import com.fr.intelli.record.Focus;
import com.fr.record.analyzer.EnableMetrics;

@EnableMetrics
public class ControllerRegister extends AbstractControllerRegisterProvider {

    @Override
    @Focus(id="com.api.hyundaicapital.user.sync", text = "user-sync")
    public Class<?>[] getControllers() {
        return new Class[]{
                PNDUserSync.class

        };
    }
}
