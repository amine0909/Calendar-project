package org.businessanddecisions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.businessanddecisions.repositories")
@EntityScan(basePackages = {"com.businessanddecisions.models"})
@ComponentScan(basePackages = {"com.businessanddecisions.controllers","com.businessanddecisions.services","config","org.businessanddecisions.common"})
@Import(value = {config.WebSecurityConfig.class})
@SpringBootApplication
public class GeneratePlanningApplication {
	public static void main(String[] args) {
		SpringApplication.run(GeneratePlanningApplication.class, args);
	}
}
