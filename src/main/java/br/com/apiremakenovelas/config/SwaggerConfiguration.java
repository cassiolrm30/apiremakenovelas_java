package br.com.apiremakenovelas.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfiguration
{
	@Bean
	public Docket api()
	{	
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("br.com.apiremakenovelas"))
				.paths(PathSelectors.ant("/**"))
				.build()
				.apiInfo(apiInfo());
	}	

	private ApiInfo apiInfo()
	{
		return new ApiInfo(
				"API de remake de novelas", 
				"Sistema Spring Boot API / Hibernate JPA",
				"Versão 1.0",
				"http://www.google.com.br",
				new Contact("Remake de Novelas", "http://www.google.com.br", "contato@google.com.br"),
				"Licença da API",
				"http://www.google.com.br",
				Collections.emptyList()
				);
	}
}