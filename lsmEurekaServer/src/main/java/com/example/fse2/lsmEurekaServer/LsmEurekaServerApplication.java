package com.example.fse2.lsmEurekaServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LsmEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LsmEurekaServerApplication.class, args);
	}

}
