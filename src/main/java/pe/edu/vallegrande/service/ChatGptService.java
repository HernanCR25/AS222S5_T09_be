package pe.edu.vallegrande.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.model.ChatGptModel;
import pe.edu.vallegrande.repository.ChatGptRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    private final WebClient webClient;
    private final ChatGptRepository chatGptRepository;

    public Mono<String> sendRequest(String requestBody) {
        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    // Reemplaza \n con saltos de línea reales
                    String formattedResponse = response.replace("\\n", System.lineSeparator());

                    ChatGptModel chatGptModel = new ChatGptModel(null, requestBody, formattedResponse, LocalDateTime.now());
                    return chatGptRepository.save(chatGptModel)  // Guarda en la base de datos
                            .then(Mono.just(formattedResponse));  // Retorna la respuesta formateada
                });
    }
    public Flux<ChatGptModel> getAllChatGptModels() {
        return chatGptRepository.findAll();
    }
}
