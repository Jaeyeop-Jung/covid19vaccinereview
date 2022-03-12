package com.teamproject.covid19vaccinereview;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy
//@EnableSwagger2
public class Covid19vaccinereviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(Covid19vaccinereviewApplication.class, args);

	}

}
