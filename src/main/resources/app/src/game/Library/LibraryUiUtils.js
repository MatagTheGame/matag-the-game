export class LibraryUiUtils {
  static libraryHeight(cardsNumber, type) {
    const extraOpponentRotation = type === 'opponent' ? 'rotateZ(180deg) ' : ''
    return {
      transform: `${extraOpponentRotation} translateZ(${cardsNumber}px)`,
    }
  }

  static libraryBottomThickness(cardsNumber) {
    const twiceCardsNumber = cardsNumber * 2
    return {
      height: `${twiceCardsNumber}px`,
      bottom: `-${cardsNumber}px`,
      transform: `rotateX(-90deg)`
    }
  }

  static libraryRightThickness(cardsNumber) {
    const twiceCardsNumber = cardsNumber * 2
    return {
      width: `${twiceCardsNumber}px`,
      right: `-${cardsNumber}px`,
      transform: `rotateY(-90deg)`
    }
  }
}