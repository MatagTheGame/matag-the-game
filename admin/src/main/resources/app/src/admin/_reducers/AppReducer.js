const clone = (object) => {
  if (!object) {
    object = {}
  }
  return JSON.parse(JSON.stringify(object))
}

export default (state, action) => {
  const newState = clone(state)

  if (!state) {
    return {}

  } else if (action.type === 'LOAD_STATS') {
    newState.stats = {loading: true}

  } else {
    throw new Error(`Unknown action type ${action.type}`)
  }

  return newState
}