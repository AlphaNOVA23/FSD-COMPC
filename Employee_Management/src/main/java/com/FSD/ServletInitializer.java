package com.FSD;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Required for deploying the Spring Boot application as a WAR file to an external 
 * application server like JBoss / WildFly.
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EmployeeManagementApplication.class);
	}

}
