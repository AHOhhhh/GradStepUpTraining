package com.thoughtworks.mall_product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MallProductApplication {
	public static void main(String[] args) {
		SpringApplication.run(MallProductApplication.class, args);
	}
}
