package dreamcompany.service.implementation.chat_message;

import dreamcompany.domain.entity.ChatMessage;
import dreamcompany.domain.model.service.ChatMessageServiceModel;
import dreamcompany.repository.ChatMessageRepository;
import dreamcompany.service.implementation.base.TestBase;
import dreamcompany.service.interfaces.ChatMessageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ChatMessageServiceTests extends TestBase {

    @Autowired
    ChatMessageService chatMessageService;

    @MockBean
    ChatMessageRepository chatMessageRepository;

    ChatMessageServiceModel chatMessage;

    @Override
    protected void before() {
        chatMessage = new ChatMessageServiceModel();
    }

    @Test
    public void create_shouldSetDateAndSaveChatMessageInDb() {

        when(chatMessageRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ChatMessageServiceModel savedAndReturned = chatMessageService.create(chatMessage);

        verify(chatMessageRepository).save(any());
        assertNotNull(savedAndReturned.getCreatedOn());
    }

    @Test
    public void updateImageUrl_shouldUpdateAllOldImageUrlsWithNew() {

        List<ChatMessage> messagesInDb = List.of(new ChatMessage(), new ChatMessage());

        when(chatMessageRepository.findAllBySender(any()))
                .thenReturn(messagesInDb);

        chatMessageService.updateImageUrl("old", "new");

        when(chatMessageRepository.saveAll(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        verify(chatMessageRepository).saveAll(any());

        messagesInDb.forEach(m -> assertEquals("new", m.getImageUrl()));
    }

    @Test
    public void findAll_shouldReturnAllChatMessagesInDbOrderedByDate_whenAny() {

        ChatMessage firstMessage = new ChatMessage();
        firstMessage.setContent("firstMessage");
        firstMessage.setCreatedOn(new Date());

        ChatMessage secondMessage = new ChatMessage();
        secondMessage.setContent("secondMessage");
        secondMessage.setCreatedOn(new Date());

        List<ChatMessage> messagesInDb = List.of(firstMessage, secondMessage);

        messagesInDb = messagesInDb.stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedOn))
                .collect(Collectors.toList());

        when(chatMessageRepository.findAll())
                .thenReturn(messagesInDb);

        List<ChatMessageServiceModel> returned = chatMessageService.findAll();

        assertEquals(messagesInDb.size(),returned.size());
        assertEquals(firstMessage.getContent(),returned.get(0).getContent());
        assertEquals(secondMessage.getContent(),returned.get(1).getContent());
    }

    @Test
    public void deleteAll_shouldDeleteAllMessagesInDB_whenAny(){

        chatMessageService.deleteAll();

        verify(chatMessageRepository).deleteAll();
    }
}
