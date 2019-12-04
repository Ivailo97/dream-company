package dreamcompany.service.implementation;

import dreamcompany.domain.entity.ChatMessage;
import dreamcompany.domain.model.service.ChatMessageServiceModel;
import dreamcompany.repository.ChatMessageRepository;
import dreamcompany.service.interfaces.ChatMessageService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ModelMapper modelMapper;

    private final ChatMessageRepository messageRepository;

    @Override
    public ChatMessageServiceModel createChatMessage(ChatMessageServiceModel chatMessage) {
        chatMessage.setCreatedOn(LocalDateTime.now());
        ChatMessage message = modelMapper.map(chatMessage, ChatMessage.class);
        messageRepository.save(message);
        return modelMapper.map(message, ChatMessageServiceModel.class);
    }

    @Override
    public List<ChatMessageServiceModel> findAll() {
        return messageRepository.findAll().stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedOn))
                .map(c -> modelMapper.map(c, ChatMessageServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        messageRepository.deleteAll();
    }
}
