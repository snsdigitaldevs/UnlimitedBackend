package com.simonschuster.pimsleur.unlimited.configs;

import com.simonschuster.pimsleur.unlimited.filter.RequestLogFilter;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
                        .allowedOrigins(
                                "https://pimsleur-web-qa.s3-website-us-east-1.amazonaws.com",
                                "https://ask-ifr-download.s3.amazonaws.com",
                                "https://d5vvzkykh1agb.cloudfront.net",
                                "https://pims-qa.dogememe.tk",
                                "https://learn.pimsleur.com",
                                "https://learn-uat.pimsleur.com",
                                "https://learn-qa.pimsleur.com",
                                "https://d22h8kdloqwqsy.cloudfront.net",
                                "https://d3u965pxjxbjuz.cloudfront.net",
                                "https://d7bcyw9tdqg2v.cloudfront.net",
                                "https://d3vu3jnt00sinm.cloudfront.net",
                                "https://d3v8tijfb44l02.cloudfront.net",
                                "https://d1dugg8e90k1bj.cloudfront.net",
                                "https://learn-dev.pimsleur.com.s3-website-us-east-1.amazonaws.com",
                                "http://pimsleur-web-qa.s3-website-us-east-1.amazonaws.com",
                                "http://ask-ifr-download.s3.amazonaws.com",
                                "http://d5vvzkykh1agb.cloudfront.net",
                                "http://pims-qa.dogememe.tk",
                                "http://learn.pimsleur.com",
                                "http://learn-uat.pimsleur.com",
                                "http://d22h8kdloqwqsy.cloudfront.net",
                                "http://d3u965pxjxbjuz.cloudfront.net",
                                "http://d3v8tijfb44l02.cloudfront.net",
                                "http://d1dugg8e90k1bj.cloudfront.net",
                                "http://learn-qa.pimsleur.com",
                                "http://learn-dev.pimsleur.com.s3-website-us-east-1.amazonaws.com",
                                "http://learn-itg.pimsleur.com.s3-website-us-east-1.amazonaws.com")
                        .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public FilterRegistrationBean<Filter> requestLogFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        RequestLogFilter requestLogFilter = new RequestLogFilter();
        requestLogFilter.setIncludeQueryString(true);
        requestLogFilter.setIncludePayload(true);
        registration.setFilter(requestLogFilter);
        registration.addUrlPatterns("/*");
        registration.setName("requestLogFilter");
        registration.setOrder(3);
        return registration;
    }
}
