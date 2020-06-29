import CardUtils from 'game/Card/CardUtils'

function card(builder = {}) {
  return {
    id: '1',
    card: {
      name: 'CardName',
      types: builder.types ? builder.types : ['CREATURE']
    },
    summoningSickness : builder.summoningSickness,
    modifiers: {
      tapped: builder.tapped
    },
    abilities: builder.abilities ? builder.abilities : []
  }
}

describe('canAttack', () => {
  test('creatures can attack', () => {
    // When
    const result = CardUtils.canAttack(card())

    // Then
    expect(result).toBe(true)
  })

  test('other types cannot attack', () => {
    // Given
    const cardInstance = card({types: ['ENCHANTMENT']})

    // When
    const result = CardUtils.canAttack(cardInstance)

    // Then
    expect(result).toBe('"1 - CardName" is not of type Creature.')
  })

  test('creatures with summoning sickness cannot attack', () => {
    // Given
    const cardInstance = card({summoningSickness: true})

    // When
    const result = CardUtils.canAttack(cardInstance)

    // Then
    expect(result).toBe('"1 - CardName" has summoning sickness and cannot attack.')
  })

  test('tapped creature cannot attack', () => {
    // Given
    const cardInstance = card({tapped: true})

    // When
    const result = CardUtils.canAttack(cardInstance)

    // Then
    expect(result).toBe('"1 - CardName" is tapped and cannot attack.')
  })

  test('creature with defender cannot attack', () => {
    // Given
    const cardInstance = card({abilities: [{abilityType: 'DEFENDER'}]})

    // When
    const result = CardUtils.canAttack(cardInstance)

    // Then
    expect(result).toBe('"1 - CardName" has defender and cannot attack.')
  })
})
