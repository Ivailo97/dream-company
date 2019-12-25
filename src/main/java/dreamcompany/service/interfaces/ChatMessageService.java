package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.ChatMessageServiceModel;

import java.util.List;

public interface ChatMessageService {

    ChatMessageServiceModel create(ChatMessageServiceModel chatMessage);

    void updateImageUrl(String sender,String newUrl);

    List<ChatMessageServiceModel> findAll();

    void deleteAll();
}
