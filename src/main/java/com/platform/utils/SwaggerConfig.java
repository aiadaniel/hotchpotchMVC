package com.platform.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2

/*
 * ����ʹ��swagger�����ߵ���api������ӿ��
 * 
 * ����û��ʹ��xml�����ļ������Ǳ��ʽbean����
 */
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("v1").select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/**"))
				.build().apiInfo(apiInfo("0.0.1"));
	}
	
	@Bean
	public Docket api2() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("v2").select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/v2/**"))
				.build().apiInfo(apiInfo("0.0.2"));
	}
	
	private ApiInfo apiInfo(String v) {
		return new ApiInfo("forum api online", "api debug", v, null, new Contact("daniel", null, null), null, null);
	}
}
