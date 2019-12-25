package dreamcompany.repository;

import dreamcompany.domain.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,String> {

    List<ChatMessage> findAllBySender(String sender);
}
