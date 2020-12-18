package com.jianfei.blog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.jianfei.blog.properties.StorageProperties;
import com.jianfei.blog.service.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			// storageService.deleteAll();
			storageService.init();
		};
	}

//	@Bean
//	public Connector connector() {
//		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//		connector.setScheme("http");
//		connector.setPort(4445);
////		connector.setSecure(false);
////		connector.setRedirectPort(443);
//		return connector;
//	}
//
//	@Bean
//	public TomcatServletWebServerFactory tomcatServletWebServerFactory(Connector connector) {
//		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//			@Override
//			protected void postProcessContext(Context context) {
//				SecurityConstraint securityConstraint = new SecurityConstraint();
//				securityConstraint.setUserConstraint("CONFIDENTIAL");
//				SecurityCollection collection = new SecurityCollection();
//				collection.addPattern("/*");
//				securityConstraint.addCollection(collection);
//				context.addConstraint(securityConstraint);
//			}
//		};
//		tomcat.addAdditionalTomcatConnectors(connector);
//		return tomcat;
//	}
}
