package com.aa.mtg.init;

import com.aa.mtg.event.Event;
import com.aa.mtg.event.EventSender;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.context.junit4.SpringRunner;

import static com.aa.mtg.utils.TestUtils.sessionHeader;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InitControllerTest {

  @Autowired
  private InitController initController;

  @Autowired
  private EventSender eventSender;

  @Before
  public void setup() {
    Mockito.reset(eventSender);
  }

  @Test
  @Ignore
  public void shouldRespondToInit() {
    // When
    SimpMessageHeaderAccessor sessionHeaderUserOne = sessionHeader("userOne");
    initController.init(sessionHeaderUserOne);

    // Then
    Event expectedWaitingOpponentEvent = new Event("INIT", "WAITING_OPPONENT");
    Mockito.verify(eventSender).sendToUser("userOne", expectedWaitingOpponentEvent);

    // When
    SimpMessageHeaderAccessor sessionHeaderUserTwo = sessionHeader("userTwo");
    initController.init(sessionHeaderUserTwo);

    Event expectedCompletedEvent = new Event("INIT", "COMPLETED");
    Mockito.verify(eventSender).sendToAll(expectedCompletedEvent);
  }
}