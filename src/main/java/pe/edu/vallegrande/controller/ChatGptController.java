package pe.edu.vallegrande.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.model.ChatGptModel;
import pe.edu.vallegrande.service.ChatGptService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin("*")
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
    @GetMapping("/{id}")
public Mono<ChatGptModel> getChatGptModelById(@PathVariable Long id) {
    return chatGptService.getChatGptModelById(id);
}

    @GetMapping("/active")
    public Flux<ChatGptModel> getActiveChatGptModels() {
        return chatGptService.getActiveChatGptModels();
    }

    @GetMapping("/inactive")
    public Flux<ChatGptModel> getInactiveChatGptModels() {
        return chatGptService.getInactiveChatGptModels();
    }

    @PutMapping("/update/{id}")
    public Mono<ChatGptModel> updateChatGptModel(@PathVariable Long id, @RequestBody String newConsulta) {
        return chatGptService.updateChatGptModel(id, newConsulta);
    }    

    @PatchMapping("/deactivate/{id}")
    public Mono<ChatGptModel> deactivateChatGptModel(@PathVariable Long id) {
        return chatGptService.deactivateChatGptModel(id);
    }

    @PatchMapping("/activate/{id}")
    public Mono<ChatGptModel> activateChatGptModel(@PathVariable Long id) {
        return chatGptService.activateChatGptModel(id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteChatGptModel(@PathVariable Long id) {
        return chatGptService.deleteChatGptModel(id);
    }
}
