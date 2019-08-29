package org.guohai.vaccine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.guohai.vaccine.dao")
public class VaccineApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccineApplication.class, args);
	}

}
