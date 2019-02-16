export class LibraryUiUtils {
  static libraryMarginBottom(cardsNumber) {
    const marginBottom = cardsNumber * 2
    return {
      marginBottom: `${marginBottom}px`,
    }
  }

  static cardBottomThickness(cardsNumber) {
    const halfCardsNumber = Math.floor(cardsNumber / 2)
    return {
      height: `${cardsNumber}px`,
      bottom: `-${halfCardsNumber}px`,
      transform: `rotateX(-45deg) translateY(${halfCardsNumber}px)`
    }
  }

  static cardRightThickness(cardsNumber) {
    const halfCardsNumber = Math.floor(cardsNumber / 2)
    return {
      width: `${cardsNumber}px`,
      right: `-${halfCardsNumber}px`,
      transform: `rotateY(110deg) translateX(${halfCardsNumber}px)`
    }
  }
}