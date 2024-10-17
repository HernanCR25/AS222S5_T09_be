package pe.edu.vallegrande.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.model.ChatGptModel;

@Repository
public interface ChatGptRepository extends ReactiveCrudRepository<ChatGptModel, Long> {
}
