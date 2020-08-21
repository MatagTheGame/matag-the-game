import TurnPhases from 'game/Turn/TurnUtils'

describe('Display Phases', () => {
  function testCase(currentPhase, expectedOutput) {
    expect(TurnPhases.phasesToRender(currentPhase)).toEqual(expectedOutput)
  }

  test('UT', () => {
    testCase('UT', ['UT', 'UP', 'DR', 'M1', '...'])
  })

  test('UP', () => {
    testCase('UP', ['UT', 'UP', 'DR', 'M1', '...'])
  })

  test('DR', () => {
    testCase('DR', ['UT', 'UP', 'DR', 'M1', '...'])
  })

  test('M1', () => {
    testCase('M1', ['...', 'DR', 'M1', 'BC', '...'])
  })

  test('BC', () => {
    testCase('BC', ['...', 'M1', 'BC', 'DA', '...'])
  })

  test('DA', () => {
    testCase('DA', ['...', 'BC', 'DA', 'DB', '...'])
  })

  test('DB', () => {
    testCase('DB', ['...', 'DA', 'DB', 'FS', '...'])
  })

  test('CD', () => {
    testCase('CD', ['...', 'FS', 'CD', 'EC', '...'])
  })

  test('EC', () => {
    testCase('EC', ['...', 'CD', 'EC', 'M2', '...'])
  })

  test('M2', () => {
    testCase('M2', ['...', 'EC', 'M2', 'ET', 'CL'])
  })

  test('ET', () => {
    testCase('ET', ['...', 'EC', 'M2', 'ET', 'CL'])
  })

  test('CL', () => {
    testCase('CL', ['...', 'EC', 'M2', 'ET', 'CL'])
  })
})
