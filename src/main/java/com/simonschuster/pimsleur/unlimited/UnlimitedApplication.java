package com.simonschuster.pimsleur.unlimited;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
@ComponentScan(basePackages = "com.simonschuster.pimsleur")
//@EnableJpaRepositories(basePackages = "com.simonschuster.pimsleur.unlimited.repo")
@EntityScan(basePackages = "com.simonschuster.pimsleur.unlimited.data.domain")
public class UnlimitedApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnlimitedApplication.class, args);
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(false);
        return loggingFilter;
    }
}
