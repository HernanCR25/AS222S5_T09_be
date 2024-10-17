package pe.edu.vallegrande.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("https://chatgpt-api8.p.rapidapi.com")
                .defaultHeader("x-rapidapi-key", "5258e2b9edmsh19d0d7a4a0cdc57p18c749jsn9a0d4349d129")
                .defaultHeader("x-rapidapi-host", "chatgpt-api8.p.rapidapi.com")
                .defaultHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
    }
}
