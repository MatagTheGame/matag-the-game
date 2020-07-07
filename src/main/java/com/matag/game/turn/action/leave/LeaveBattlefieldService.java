package com.matag.game.turn.action.leave;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.attach.AttachService;
import com.matag.game.turn.action.attach.AttachmentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static com.matag.cards.properties.Type.ARTIFACT;
import static com.matag.cards.properties.Type.ENCHANTMENT;

@Component
public class LeaveBattlefieldService {
  private static final Logger LOGGER = LoggerFactory.getLogger(LeaveBattlefieldService.class);

  private final AttachService attachService;
  private final AttachmentsService attachmentsService;
  private final DestroyPermanentService destroyPermanentService;

  @Lazy
  public LeaveBattlefieldService(AttachService attachService, AttachmentsService attachmentsService, DestroyPermanentService destroyPermanentService) {
    this.attachService = attachService;
    this.attachmentsService = attachmentsService;
    this.destroyPermanentService = destroyPermanentService;
  }

  public void leaveTheBattlefield(GameStatus gameStatus, int permanentId) {
    var cardInstance = gameStatus.extractCardByIdFromAnyBattlefield(permanentId);
    cardInstance.resetAllModifiers();

    for (var attachedCard : attachmentsService.getAttachedCards(gameStatus, cardInstance)) {
      if (attachedCard.isOfType(ENCHANTMENT)) {
        destroyPermanentService.markToBeDestroyed(gameStatus, attachedCard.getId());

      } else if (attachedCard.isOfType(ARTIFACT)) {
        attachService.detach(cardInstance, attachedCard);
      }
    }

    LOGGER.info("{} left the battlefield.", cardInstance.getIdAndName());
  }
}
