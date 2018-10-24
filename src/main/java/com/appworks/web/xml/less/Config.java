/**
 * Copyright © 2017 Open Text.  All Rights Reserved.
 */
package com.appworks.web.xml.less;

import com.appworks.web.xml.less.services.GatewaySdkBean;
import com.appworks.web.xml.less.services.GatewaySdkBeanImpl;
import com.appworks.web.xml.less.services.settings.SettingsBean;
import com.opentext.otag.sdk.client.v3.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Provide the SDK clients as beans to the application.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.appworks.web.xml.less")
public class Config {

    @Bean
    public GatewaySdkBean gatewaySdkBean() {
        return new GatewaySdkBeanImpl();
    }

    @Bean
    public SettingsBean settingsBean() {
        return new SettingsBean();
    }

    // AppWorks SDK Client beans

    @Bean
    public AuthClient authClient() {
        return new AuthClient();
    }

    @Bean
    public MailClient mailClient() {
        return new MailClient();
    }

    @Bean
    public NotificationsClient notificationsClient() {
        return new NotificationsClient();
    }

    @Bean
    public RuntimesClient runtimesClient() {
        return new RuntimesClient();
    }

    @Bean
    public ServiceClient serviceClient() {
        return new ServiceClient();
    }

    @Bean
    public SettingsClient settingsClient() {
        return new SettingsClient();
    }

    @Bean
    public TrustedProviderClient trustedProviderClient() {
        return new TrustedProviderClient();
    }

}
