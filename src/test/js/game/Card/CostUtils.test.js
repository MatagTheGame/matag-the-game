import CostUtils from 'game/Card/CostUtils'

describe('isCostFulfilled', () => {
  test('isCostFulfilled simple mana color', () => {
    // Given
    const cost = ['WHITE']
    const currentTappedMana = {1: 'WHITE'}

    // When
    const fulfilled = CostUtils.isCostFulfilled(cost, currentTappedMana)

    // Then
    expect(fulfilled).toBe(true)
  })

  test('isCostFulfilled two colors and any color fulfilled', () => {
    // Given
    const cost = ['WHITE', 'RED', 'ANY']
    const currentTappedMana = {1: 'WHITE', 2: 'WHITE', 3: 'RED'}

    // When
    const fulfilled = CostUtils.isCostFulfilled(cost, currentTappedMana)

    // Then
    expect(fulfilled).toBe(true)
  })

  test('isCostFulfilled two colors and any color not fulfilled', () => {
    // Given
    const cost = ['WHITE', 'ANY', 'ANY']
    const currentTappedMana = {1: 'WHITE'}

    // When
    const fulfilled = CostUtils.isCostFulfilled(cost, currentTappedMana)

    // Then
    expect(fulfilled).toBe(false)
  })

  test('isCostFulfilled colorless fulfilled', () => {
    // Given
    const cost = ['COLORLESS']
    const currentTappedMana = {1: 'COLORLESS'}

    // When
    const fulfilled = CostUtils.isCostFulfilled(cost, currentTappedMana)

    // Then
    expect(fulfilled).toBe(true)
  })

  test('isCostFulfilled colorless not fulfilled', () => {
    // Given
    const cost = ['COLORLESS']
    const currentTappedMana = {1: 'WHITE'}

    // When
    const fulfilled = CostUtils.isCostFulfilled(cost, currentTappedMana)

    // Then
    expect(fulfilled).toBe(false)
  })

  test('getDisplayMana', () => {
    // Given
    const mana = ['WHITE', 'ANY', 'COLORLESS']

    // When
    const displayMana = CostUtils.getDisplayMana(mana)

    // Then
    expect(displayMana).toEqual(['2', 'WHITE'])
  })
})
