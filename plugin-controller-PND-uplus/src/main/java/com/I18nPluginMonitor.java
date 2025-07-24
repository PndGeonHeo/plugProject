package com;

import com.fr.cluster.ClusterBridge;
import com.fr.cluster.core.ClusterNode;
import com.fr.decision.authority.AuthorityContext;
import com.fr.decision.authority.data.User;
import com.fr.decision.service.DecisionServiceManager;
import com.fr.decision.service.authority.DecisionUserServiceProvider;
import com.fr.decision.service.system.DecisionMessageServiceProvider;
import com.fr.decision.system.bean.message.SystemMessage;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.observer.inner.AbstractPluginLifecycleMonitor;
import com.fr.stable.query.QueryFactory;
import com.fr.stable.web.ServletContext;
import com.fr.stable.web.ServletContextAdapter;

import java.util.HashSet;
import java.util.Iterator;

/**
 * @author zack
 * @version 10.0
 * Created by zack on 2022/7/1
 */
public class I18nPluginMonitor extends AbstractPluginLifecycleMonitor {
    @Override
    public void afterRun(PluginContext pluginContext) {
        FineLoggerFactory.getLogger().error("notification");
                ServletContext.addServletContextListener(new ServletContextAdapter() {
                    @Override
                    public void onServletStart() {
                        String clusterStr = "";
                        String resultStr = "UPLUS 플러그인 정상 구동 완료되었습니다.";
                        try {
                            ClusterNode clusterNode = ClusterBridge.getView().getCurrent();
                            if (ClusterBridge.getConfig().isCluster()) {
                                clusterStr = clusterNode.getName() + "";
                            }
                        }catch (Exception eee){
                            FineLoggerFactory.getLogger().error("Cluster getName Fault");
                        }

                        String finalClusterStr = clusterStr;
                        //serviceServlet 추가
                        DecisionUserServiceProvider userservice = DecisionServiceManager.getInstance().getService(DecisionUserServiceProvider.class);
                        DecisionMessageServiceProvider messageService = DecisionServiceManager.getInstance().getService(DecisionMessageServiceProvider.class);

                        try {
                            Iterator var5 = AuthorityContext.getInstance().getUserController().findIn("id", new HashSet(userservice.getAdminUserIdList()), QueryFactory.create()).iterator();
                            while(var5.hasNext()) {
                                User user = (User) var5.next();
                                SystemMessage message = new SystemMessage(user.getId(), user.getUserName(), finalClusterStr + resultStr, "ATEST", "", 1L,0);
                                messageService.saveMessage(message);
                            }
                        } catch (Exception igig) {
                            FineLoggerFactory.getLogger().error(igig.toString());
                        }
                        FineLoggerFactory.getLogger().error("--------------notification ");
                    }
                });


    }


    @Override
    public void beforeStop(PluginContext pluginContext) {


    }
}