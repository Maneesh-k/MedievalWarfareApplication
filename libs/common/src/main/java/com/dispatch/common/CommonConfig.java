package com.dispatch.common;

import java.util.Map;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class CommonConfig {

	@Bean
	public OpenAPI openAPIConfig() {
		String SECURITY_SCHEME_NAME = HttpHeaders.AUTHORIZATION;

		SecurityScheme securityScheme = new SecurityScheme()
				.type(SecurityScheme.Type.APIKEY)
				.name(SECURITY_SCHEME_NAME)
				.in(SecurityScheme.In.HEADER);

		Map<String, SecurityScheme> securitySchemes = Map.of(SECURITY_SCHEME_NAME, securityScheme);

		return new OpenAPI()
				.components(new Components().securitySchemes(securitySchemes))
				.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
	}
}
