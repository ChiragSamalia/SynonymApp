package com.Assignment.Synonym;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages="com.Assignment.Synonym.Repo.")
@ComponentScan(basePackages = {"com.Assignment.Synonym.Repo.","com.Assignment.Synonym.Service.","com.Assignment.Synonym.Dto.",
		"com.Assignment.Synonym.Controller.","com.Assignment.Synonym.Security."})

public class SynonymAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SynonymAppApplication.class, args);
	}

}
