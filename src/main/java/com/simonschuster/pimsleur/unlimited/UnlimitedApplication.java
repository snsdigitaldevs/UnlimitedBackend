package com.simonschuster.pimsleur.unlimited;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.simonschuster.pimsleur.unlimited")
@EnableJpaRepositories(basePackages = "com.simonschuster.pimsleur.unlimited.repo")
@EntityScan(basePackages = "com.simonschuster.pimsleur.unlimited.data.domain")
public class UnlimitedApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnlimitedApplication.class, args);
	}
}
