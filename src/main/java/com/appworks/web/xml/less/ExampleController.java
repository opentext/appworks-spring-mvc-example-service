/**
 * Copyright © 2017 Open Text.  All Rights Reserved.
 */
package com.appworks.web.xml.less;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExampleController {

    /**
     * Once deployed and enabled we should be able to hit this service on
     * {@code <host>/mvc-test-service/api/}.
     *
     * @return a welcome message
     */
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    @ResponseBody
    public String welcome() {
        return "AppWorks web.xml-less Service deployed successfully";
    }

}
