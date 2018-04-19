package com.simonschuster.pimsleur.unlimited.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:config.properties")
public class ApplicationConfiguration {

    @Autowired
    private Environment env;

    public String getApiParameter(String propertyName) {
        return env.getProperty("edt.api.parameters." + propertyName);
    }

    public String getProperty(String propertyName){
        return env.getProperty(propertyName);
    }
}