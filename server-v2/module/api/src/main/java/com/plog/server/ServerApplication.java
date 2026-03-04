package com.plog.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.plog")
public class ServerApplication {

	static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
