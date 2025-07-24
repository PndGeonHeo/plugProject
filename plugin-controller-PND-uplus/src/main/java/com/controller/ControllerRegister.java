package com.controller;

import com.fr.decision.fun.impl.AbstractControllerRegisterProvider;
import com.fr.intelli.record.Focus;
import com.fr.record.analyzer.EnableMetrics;

@EnableMetrics
public class ControllerRegister extends AbstractControllerRegisterProvider {

    @Override
    @Focus(id="com.controller.PND.uplus", text = "uplus controller")
    public Class<?>[] getControllers() {
        return new Class[]{
                com.controller.Common.class,
                com.controller.Skill.class,
                com.controller.Agent.class,
                com.controller.Test.class,
                com.controller.SkillManage.class,

                com.controller.v2.Common.class,
                com.controller.v2.Queue.class,
                com.controller.v2.Skill.class,
                com.controller.v2.Agent.class
        };
    }
}
