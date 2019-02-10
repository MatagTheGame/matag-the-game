package com.aa.mtg.init;

import com.aa.mtg.event.Event;
import com.aa.mtg.event.EventSender;
import com.aa.mtg.user.UserRepository;
import org.junit.Before;
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

  @Autowired
  private UserRepository userRepository;

  @Before
  public void setup() {
    Mockito.reset(eventSender);
    userRepository.removeAllUsers();
  }

  @Test
  public void shouldRespondToInit() {
    // Given
    SimpMessageHeaderAccessor sessionHeader = sessionHeader("sessionId");

    // When
    initController.init(sessionHeader);

    // Then
    Event expectedEvent = Event.builder().type("INIT").value("COMPLETED").build();
    Mockito.verify(eventSender).sendToUser("sessionId", expectedEvent);
  }
}