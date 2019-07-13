/* global describe, expect */

import CostUtils from 'Main/game/Card/CostUtils'

describe('isCostFulfilled', () => {
  test('isCostFulfilled simple mana color', () => {
    // Given
    const cost = ['WHITE']
    const manaPaid = ['WHITE']

    // When
    const fulfilled = CostUtils.isCostFulfilled(cost, manaPaid)

    // Then
    expect(fulfilled).toBe(true)
  })

  test('isCostFulfilled two colors and colorless fulfilled', () => {
    // Given
    const cost = ['WHITE', 'RED', 'COLORLESS']
    const manaPaid = ['WHITE', 'WHITE', 'RED']

    // When
    const fulfilled = CostUtils.isCostFulfilled(cost, manaPaid)

    // Then
    expect(fulfilled).toBe(true)
  })

  test('isCostFulfilled two colors and colorless not fulfilled', () => {
    // Given
    const cost = ['WHITE', 'COLORLESS', 'COLORLESS']
    const manaPaid = ['WHITE']

    // When
    const fulfilled = CostUtils.isCostFulfilled(cost, manaPaid)

    // Then
    expect(fulfilled).toBe(false)
  })
})
