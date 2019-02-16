export class LibraryUiUtils {
  static cardHeight(cardsNumber) {
    const halfCardsNumber = Math.floor(cardsNumber / 2)
    return {
      transform: `translateZ(${halfCardsNumber}px)`,
    }
  }

  static cardBottomThickness(cardsNumber) {
    const halfCardsNumber = Math.floor(cardsNumber / 2)
    return {
      height: `${cardsNumber}px`,
      bottom: `-${halfCardsNumber}px`,
      transform: `rotateX(-90deg)`
    }
  }

  static cardRightThickness(cardsNumber) {
    const halfCardsNumber = Math.floor(cardsNumber / 2)
    return {
      width: `${cardsNumber}px`,
      right: `-${halfCardsNumber}px`,
      transform: `rotateY(-90deg)`
    }
  }
}