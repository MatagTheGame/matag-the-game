import UserInterfaceUtils from 'game/UserInterface/UserInterfaceUtils'

describe('Scry', () => {
  function visibleLibraryBuilder(ids) {
    return ids.map((id) => {return {id: id}})
  }

  test('scry 1 top', () => {
    // Given
    const visibleLibrary = visibleLibraryBuilder([61])

    // When
    const choice = UserInterfaceUtils.scry('1', visibleLibrary, 61, 'TOP')

    // Then
    expect(choice).toBe('1')
  })

  test('scry 1 bottom', () => {
    // Given
    const visibleLibrary = visibleLibraryBuilder([61])

    // When
    const choice = UserInterfaceUtils.scry('1', visibleLibrary, 61, 'BOTTOM')

    // Then
    expect(choice).toBe('-1')
  })

  test('scry 5 positive top', () => {
    // Given
    const visibleLibrary = visibleLibraryBuilder([61, 62, 63, 64, 65])

    // When
    const choice = UserInterfaceUtils.scry('1,2,3,4,5', visibleLibrary, 63, 'TOP')

    // Then
    expect(choice).toBe('2,3,1,4,5')
  })

  test('scry 5 positive bottom', () => {
    // Given
    const visibleLibrary = visibleLibraryBuilder([61, 62, 63, 64, 65])

    // When
    const choice = UserInterfaceUtils.scry('1,2,3,4,5', visibleLibrary, 62, 'BOTTOM')

    // Then
    expect(choice).toBe('1,-1,2,3,4')
  })

  test('scry 5 positive top again', () => {
    // Given
    const visibleLibrary = visibleLibraryBuilder([61, 62, 63, 64, 65])

    // When
    const choice = UserInterfaceUtils.scry('-1,-2,1,2,3', visibleLibrary, 65, 'TOP')

    // Then
    expect(choice).toBe('-1,-2,2,3,1')
  })

  test('scry 5 positive bottom again', () => {
    // Given
    const visibleLibrary = visibleLibraryBuilder([61, 62, 63, 64, 65])

    // When
    const choice = UserInterfaceUtils.scry('-1,-2,1,2,3', visibleLibrary, 65, 'BOTTOM')

    // Then
    expect(choice).toBe('-2,-3,1,2,-1')
  })

  test('scry 5 negative top', () => {
    // Given
    const visibleLibrary = visibleLibraryBuilder([61, 62, 63, 64, 65])

    // When
    const choice = UserInterfaceUtils.scry('-1,-2,-3,1,2', visibleLibrary, 62, 'TOP')

    // Then
    expect(choice).toBe('-1,1,-2,2,3')
  })

  test('scry 5 negative bottom', () => {
    // Given
    const visibleLibrary = visibleLibraryBuilder([61, 62, 63, 64, 65])

    // When
    const choice = UserInterfaceUtils.scry('-1,-2,1,2,3', visibleLibrary, 62, 'BOTTOM')

    // Then
    expect(choice).toBe('-2,-1,1,2,3')
  })
})
