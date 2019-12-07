package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.ChatMessageServiceModel;

import java.util.List;

public interface ChatMessageService {

    ChatMessageServiceModel createChatMessage(ChatMessageServiceModel chatMessage);

    void updateImageUrl(String oldUrl,String newUrl);

    List<ChatMessageServiceModel> findAll();

    void deleteAll();
}
