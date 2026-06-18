package com.wildace.poker.jacksorbetter.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI defineOpenApi() {
		Server server = new Server();
		server.setUrl("http://192.168.1.127:9080");
		server.setDescription("Video Poker Game Service API Server");
		
		Contact contact = new Contact();
		contact.setName("WildAce Team");
		contact.setEmail("ops@wildace.in");
		
		Info info = new Info()
				.title("Video Poker Service API")
				.description("RESET API for Video Poker Game Service")
				.version("1.0.0")
				.contact(contact);
		
		return new OpenAPI()
				.addServersItem(server)
				.info(info);
		
	}
}
