package com.bleizing.recovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.bleizing.recovery.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class RecoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecoveryApplication.class, args);
	}

}
