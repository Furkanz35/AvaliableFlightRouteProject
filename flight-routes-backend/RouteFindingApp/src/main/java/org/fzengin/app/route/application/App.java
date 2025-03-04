package org.fzengin.app.route.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "org.fzengin")
@EnableJpaRepositories(basePackages = "org.fzengin.app.route.repository")
@EntityScan(basePackages = "org.fzengin.app.route.repository.entity")

public class App {
	public static void main(String[] args)
	{
		SpringApplication.run(App.class, args);

	}
}
