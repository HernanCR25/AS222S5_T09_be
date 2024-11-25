package pe.edu.vallegrande.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.model.ChatGptModel;
import pe.edu.vallegrande.repository.ChatGptRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
                    String userQuery = extractUserQuery(requestBody);
                    String chatGptResponse = extractChatGptResponse(response);
                    ChatGptModel chatGptModel = new ChatGptModel(null, userQuery, chatGptResponse, LocalDateTime.now(), "A");
                    return chatGptRepository.save(chatGptModel)
                            .then(Mono.just(chatGptResponse));
                });
    }

    private String extractUserQuery(String requestBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(requestBody);
            for (JsonNode message : rootNode) {
                if ("user".equals(message.path("role").asText())) {
                    return message.path("content").asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String extractChatGptResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.path("text").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // Obtener todos los registros de ChatGpt
    public Flux<ChatGptModel> getAllChatGptModels() {
        return chatGptRepository.findAll();
    }

    // Obtener ChatGpt por estado activo
    public Flux<ChatGptModel> getActiveChatGptModels() {
        return chatGptRepository.findAll().filter(chatGpt -> "A".equals(chatGpt.getEstado()));
    }

    // Obtener ChatGpt por estado inactivo
    public Flux<ChatGptModel> getInactiveChatGptModels() {
        return chatGptRepository.findAll().filter(chatGpt -> "I".equals(chatGpt.getEstado()));
    }

    // Crear un nuevo ChatGpt
    public Mono<ChatGptModel> createChatGptModel(ChatGptModel chatGptModel) {
        chatGptModel.setEstado("A"); // Por defecto está activo
        return chatGptRepository.save(chatGptModel);
    }
    // Obtener ChatGpt por id
    public Mono<ChatGptModel> getChatGptModelById(Long id) {
        return chatGptRepository.findById(id);
    }
    
    // Actualizar la consulta y obtener la nueva respuesta
    public Mono<ChatGptModel> updateChatGptModel(Long id, String newConsulta) {
        return chatGptRepository.findById(id)
                .flatMap(chatGpt -> {
                    chatGpt.setConsulta(newConsulta); // Actualizamos solo la consulta
                    chatGpt.setHora(LocalDateTime.now()); // Actualizamos la hora de la consulta

                    // Llamamos al servicio de ChatGPT para obtener la nueva respuesta
                    return getChatGptResponse(newConsulta)
                            .flatMap(response -> {
                                chatGpt.setRespuesta(response);  // Actualizamos la respuesta con la nueva respuesta
                                return chatGptRepository.save(chatGpt); // Guardamos el modelo actualizado
                            });
                });
    }

    // Método para obtener la respuesta de ChatGPT
    private Mono<String> getChatGptResponse(String consulta) {
        // Aquí haces la llamada a la API de ChatGPT con la consulta y recibes la respuesta
        return webClient.post()
                .bodyValue(consulta)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    // Extrae la respuesta de ChatGPT de la respuesta del cuerpo de la solicitud
                    return Mono.just(extractChatGptResponse(response));
                });
    }

    // Inactivar un ChatGpt por ID (eliminación lógica)
    public Mono<ChatGptModel> deactivateChatGptModel(Long id) {
        return chatGptRepository.findById(id)
                .flatMap(chatGpt -> {
                    chatGpt.setEstado("I"); // Cambia el estado a "Inactivo"
                    return chatGptRepository.save(chatGpt);
                });
    }

    // Activar un ChatGpt por ID
    public Mono<ChatGptModel> activateChatGptModel(Long id) {
        return chatGptRepository.findById(id)
                .flatMap(chatGpt -> {
                    chatGpt.setEstado("A"); // Cambia el estado a "Activo"
                    return chatGptRepository.save(chatGpt);
                });
    }

    // Eliminar un ChatGpt físicamente
    public Mono<Void> deleteChatGptModel(Long id) {
        return chatGptRepository.deleteById(id);
    }
}
