package com.example.microserviceMoto;

import com.example.microserviceMoto.properties.FileUploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties({
		FileUploadProperties.class
})
public class MicroserviceMotoApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicroserviceMotoApplication.class, args);
	}

}
