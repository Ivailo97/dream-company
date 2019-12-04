package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.ChatMessageServiceModel;

import java.util.List;

public interface ChatMessageService {

    ChatMessageServiceModel createChatMessage(ChatMessageServiceModel chatMessage);

    List<ChatMessageServiceModel> findAll();

    void deleteAll();
}
