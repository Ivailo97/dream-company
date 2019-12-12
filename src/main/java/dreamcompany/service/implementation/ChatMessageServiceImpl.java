package dreamcompany.service.implementation;

import dreamcompany.domain.entity.ChatMessage;
import dreamcompany.domain.model.service.ChatMessageServiceModel;
import dreamcompany.repository.ChatMessageRepository;
import dreamcompany.service.interfaces.ChatMessageService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ModelMapper modelMapper;

    private final ChatMessageRepository messageRepository;

    @Override
    public ChatMessageServiceModel create(ChatMessageServiceModel chatMessage) {

        chatMessage.setCreatedOn(new Date());

        ChatMessage message = modelMapper.map(chatMessage, ChatMessage.class);

        messageRepository.save(message);

        return modelMapper.map(message, ChatMessageServiceModel.class);
    }

    @Override
    public void updateImageUrl(String oldUrl, String newUrl) {

        List<ChatMessage> messages = messageRepository.findAllByImageUrl(oldUrl);

        messages.forEach(m -> m.setImageUrl(newUrl));

        messageRepository.saveAll(messages);
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
