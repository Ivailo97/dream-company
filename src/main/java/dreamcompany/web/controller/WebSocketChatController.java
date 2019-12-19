package dreamcompany.web.controller;

import dreamcompany.domain.entity.ChatMessage;
import dreamcompany.domain.model.rest.ChatMessageRestModel;
import dreamcompany.domain.model.service.ChatMessageServiceModel;
import dreamcompany.domain.model.view.ChatMessageViewModel;
import dreamcompany.service.interfaces.ChatMessageService;
import dreamcompany.service.interfaces.UserService;
import dreamcompany.util.MappingConverter;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class WebSocketChatController extends BaseController {

    private final UserService userService;

    private final ChatMessageService messageService;

    private final MappingConverter converter;

    @PostMapping("/createMessage")
    public void create(@RequestBody ChatMessageRestModel model){
        messageService.create(converter.map(model, ChatMessageServiceModel.class));
    }

    @PostMapping("/clearHistory")
    public void clearHistory(){
        messageService.deleteAll();
    }

    @GetMapping("/chat")
    public ModelAndView chat(Principal principal, HttpSession session,ModelAndView modelAndView){
        String loggedUserImgUrl = userService.findByUsername(principal.getName()).getImageUrl();
        session.setAttribute("imgUrl",loggedUserImgUrl== null ? "/img/default-avatar.png": loggedUserImgUrl);
        List<ChatMessageViewModel> viewModels =messageService.findAll().stream()
                .map(c-> converter.map(c,ChatMessageViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("chatMessages",viewModels);
        return view("chat",modelAndView);
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/javainuse")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/javainuse")
    public ChatMessage newUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
