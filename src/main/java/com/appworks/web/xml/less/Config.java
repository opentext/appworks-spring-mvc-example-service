/**
 * Copyright © 2017 Open Text.  All Rights Reserved.
 */
package com.appworks.web.xml.less;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.appworks.web.xml.less")
public class Config {

}
