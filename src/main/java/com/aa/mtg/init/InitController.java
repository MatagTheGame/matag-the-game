package com.aa.mtg.init;

import com.aa.mtg.event.Event;
import com.aa.mtg.event.EventSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class InitController {

    private EventSender eventSender;

    public InitController(EventSender eventSender) {
        this.eventSender = eventSender;
    }

    @MessageMapping("/init")
    public void init(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        log.info("Init request received for sessionId '{}'", sessionId);

        eventSender.sendToUser(sessionId, Event.builder()
                .type("INIT")
                .value("COMPLETED")
                .build());
    }

}
