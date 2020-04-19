/* global describe, expect */

import DateUtils from 'admin/utils/DateUtils'

describe('isCostFulfilled', () => {
  test('isCostFulfilled simple mana color', () => {
    // Given
    const string = '2020-04-18T16:11:43.692988'

    // When
    const date = DateUtils.parse(string)
    const formatted = DateUtils.formatDateTime(date)

    // Then
    expect(formatted).toBe('4/18/2020, 4:11:43 PM')
  })
})
