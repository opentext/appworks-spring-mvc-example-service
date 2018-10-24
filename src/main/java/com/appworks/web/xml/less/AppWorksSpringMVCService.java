/**
 * Copyright © 2017 Open Text.  All Rights Reserved.
 */
package com.appworks.web.xml.less;

import com.opentext.otag.sdk.client.v3.ServiceClient;
import com.opentext.otag.sdk.handlers.AWServiceContextHandler;
import com.opentext.otag.sdk.handlers.AWServiceStartupComplete;
import com.opentext.otag.sdk.types.v3.api.error.APIException;
import com.opentext.otag.sdk.types.v3.management.DeploymentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class AppWorksSpringMVCService implements AWServiceContextHandler {

    public static final String APP_NAME = "appworks-spring-mvc-service";

    private static final Logger LOG = LoggerFactory.getLogger(AppWorksSpringMVCService.class);

    // mark this method as the one that completes the deployment
    @AWServiceStartupComplete
    @Override
    public void onStart(String appName) {
        bootstrapService(appName);
    }

    @Override
    public void onStop(String appName) {
        LOG.info("AppWorksSpringMVCService#onStop() called for \"" + appName + "\"");
    }

    private void bootstrapService(String appName) {
        LOG.info("AppWorksSpringMVCService#onStart() - initializing service \"" + appName + "\"");
        ServiceClient serviceClient;
        try {
            serviceClient = new ServiceClient();
        } catch (Exception e) {
            throw new RuntimeException("Utter failure, we could not construct a " +
                    "service client to tell the Gateway anything");
        }

        try {
            LOG.info("Attempting to complete deployment of " + appName);
            // make sure we let the Gateway know we have completed our startup
            serviceClient.completeDeployment(new DeploymentResult(true));
            LOG.info("AppWorksSpringMVCService#onStart() completed");
        } catch (Exception e) {
            processBootstrapFailure(appName, serviceClient, e);
        }
    }

    private void processBootstrapFailure(String appName, ServiceClient serviceClient, Exception e) {
        LOG.error("We failed to report the deployment completed for app {}", appName);
        if (e instanceof APIException) {
            LOG.error("SDK call failed - {}", ((APIException) e).getCallInfo());
            throw new RuntimeException("Failed to report deployment outcome ", e);
        }
        try {
            // explicitly tell the Gateway we have failed
            serviceClient.completeDeployment(
                    new DeploymentResult(appName + " deployment failed," + e.getMessage()));
            LOG.info(String.format("%s deployment failed", appName), e);
        } catch (APIException e1) {
            // API was unreachable
            throw new RuntimeException("Failed to report deployment outcome", e1);
        }
    }

}
