package com.thoughtworks.mall_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MallUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallUserApplication.class, args);
	}
}
