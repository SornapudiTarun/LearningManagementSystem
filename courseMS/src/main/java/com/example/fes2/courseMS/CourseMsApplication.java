package com.example.fes2.courseMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CourseMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseMsApplication.class, args);
	}

}
