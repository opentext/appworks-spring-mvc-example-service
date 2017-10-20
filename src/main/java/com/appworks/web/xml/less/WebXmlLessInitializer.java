package com.appworks.web.xml.less;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.Set;

public class WebXmlLessInitializer implements WebApplicationInitializer, ServletContainerInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(Config.class);

        servletContext.addListener(new ContextLoaderListener(ctx));

        // deploy the application on {host}/appworks-spring-mvc-service/api/
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
        servlet.addMapping("/api/*");
        servlet.setLoadOnStartup(1);
    }

    @Override
    public void onStartup(Set<Class<?>> arg0, ServletContext arg1) throws ServletException {
        onStartup(arg1);
    }

}
