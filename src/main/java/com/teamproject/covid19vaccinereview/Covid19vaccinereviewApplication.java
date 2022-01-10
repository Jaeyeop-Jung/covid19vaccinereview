package com.teamproject.covid19vaccinereview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Covid19vaccinereviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(Covid19vaccinereviewApplication.class, args);
	}

}
