package pe.edu.vallegrande.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${RAPIDAPI_KEY}") 
    private String rapidApiKey;

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("https://chatgpt-api8.p.rapidapi.com")
                .defaultHeader("x-rapidapi-key", rapidApiKey)  
                .defaultHeader("x-rapidapi-host", "chatgpt-api8.p.rapidapi.com")
                .defaultHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
    }
}