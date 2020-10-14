package com.example.microserviceVoiture;

import com.example.microserviceVoiture.properties.FileUploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties({
		FileUploadProperties.class
})
public class MicroserviceVoitureApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicroserviceVoitureApplication.class, args);
	}

}
