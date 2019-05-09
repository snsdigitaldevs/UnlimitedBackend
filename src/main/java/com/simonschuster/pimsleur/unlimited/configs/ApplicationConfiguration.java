package com.simonschuster.pimsleur.unlimited.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConfigurationProperties
public class ApplicationConfiguration {

    @Autowired
    private Environment env;

    public String getApiParameter(String propertyName) {
        return env.getProperty("edt.api.parameters." + propertyName);
    }

    public String getAuth0ApiParameter(String propertyName) {
        return env.getProperty("oauth.login.api.parameters." + propertyName);
    }

    public String getProperty(String propertyName) {
        return env.getProperty(propertyName);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://pimsleur-web-qa.s3-website-us-east-1.amazonaws.com",
                                "https://ask-ifr-download.s3.amazonaws.com",
                                "https://d5vvzkykh1agb.cloudfront.net",
                                "https://pims-qa.dogememe.tk",
                                "https://learn.pimsleur.com",
                                "https://learn-uat.pimsleur.com",
                                "https://learn-qa.pimsleur.com",
                                "http://pimsleur-web-qa.s3-website-us-east-1.amazonaws.com",
                                "http://ask-ifr-download.s3.amazonaws.com",
                                "http://d5vvzkykh1agb.cloudfront.net",
                                "http://pims-qa.dogememe.tk",
                                "http://learn.pimsleur.com",
                                "http://learn-uat.pimsleur.com",
                                "http://learn-qa.pimsleur.com")
                        .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);
            }
        };
    }
}