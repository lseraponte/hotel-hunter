package com.lseraponte.cupidapi.hh;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
		info = @Info(title = "Hotel Hunter Service API", version = "1.0", description = "API Documentation for Hotel Hunter"),
		tags = {
				@Tag(name = "Cupid API", description = "Cupid API Endpoints"),
				@Tag(name = "Hotel API", description = "Hotel Management Endpoints")
		}
)
@SpringBootApplication
@EnableScheduling
public class HhApplication {

	public static void main(String[] args) {
		SpringApplication.run(HhApplication.class, args);
	}

}
