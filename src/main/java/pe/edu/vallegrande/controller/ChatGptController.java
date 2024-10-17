package pe.edu.vallegrande.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.model.ChatGptModel;
import pe.edu.vallegrande.service.ChatGptService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatGptController {

    private final ChatGptService chatGptService;

    @PostMapping("/ask")
    public Mono<String> askChatGpt(@RequestBody String requestBody) {
        return chatGptService.sendRequest(requestBody);
    }
    @GetMapping
    public Flux<ChatGptModel> getAllChatGptModels() {
        return chatGptService.getAllChatGptModels();
    }
}

