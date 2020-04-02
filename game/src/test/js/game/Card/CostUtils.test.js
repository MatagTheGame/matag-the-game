/* global describe, expect */

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

  test('isCostFulfilled two colors and colorless fulfilled', () => {
    // Given
    const cost = ['WHITE', 'RED', 'COLORLESS']
    const currentTappedMana = {1: 'WHITE', 2: 'WHITE', 3: 'RED'}

    // When
    const fulfilled = CostUtils.isCostFulfilled(cost, currentTappedMana)

    // Then
    expect(fulfilled).toBe(true)
  })

  test('isCostFulfilled two colors and colorless not fulfilled', () => {
    // Given
    const cost = ['WHITE', 'COLORLESS', 'COLORLESS']
    const currentTappedMana = {1: 'WHITE'}

    // When
    const fulfilled = CostUtils.isCostFulfilled(cost, currentTappedMana)

    // Then
    expect(fulfilled).toBe(false)
  })
})
