/**
 * Copyright © 2017 Open Text.  All Rights Reserved.
 */
package com.appworks.web.xml.less;

import com.appworks.web.xml.less.services.GatewaySdkBean;
import com.appworks.web.xml.less.services.settings.SettingsBean;
import com.opentext.otag.sdk.types.v3.TrustedProviders;
import com.opentext.otag.sdk.types.v3.apps.Runtimes;
import com.opentext.otag.sdk.types.v3.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExampleController {

    @Autowired
    private GatewaySdkBean gatewaySdkBean;

    @Autowired
    private SettingsBean settingsBean;

    /**
     * Once deployed and <strong>enabled</strong> we should be able to hit this service on
     * {@code <host>/mvc-test-service/api/}.
     *
     * @return a welcome message
     */
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    @ResponseBody
    public String welcome() {
        return "AppWorks web.xml-less Service deployed successfully";
    }

    @RequestMapping(value = "/providers", method = RequestMethod.GET)
    @ResponseBody
    public TrustedProviders provider() {
        return gatewaySdkBean.getTrustedProviders();
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    @ResponseBody
    public Settings settings() {
        return settingsBean.createAndGetServiceSettings();
    }

    @RequestMapping(value = "/runtimes", method = RequestMethod.GET)
    @ResponseBody
    public Runtimes runtimes() {
        return gatewaySdkBean.getKnownRuntimes();
    }

    @RequestMapping(value = "/mail", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity sendMail(@RequestParam("from") String from,
                                   @RequestParam("to") String to,
                                   @RequestParam(value = "async", defaultValue = "true") boolean sendAsync) {
        return gatewaySdkBean.sendTestEmail(from, to, sendAsync);
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity sendNotification(@RequestParam("username") String username,
                                           @RequestParam(value = "push", defaultValue = "true") boolean sendPush) {
        return gatewaySdkBean.sendTestNotification(username, sendPush);
    }

}
