import ServerEventsReducer from './ServerEventsReducer'
import ClientEventsReducer from './ClientEventsReducer'

const clone = (object) => {
  if (!object) {
    object = {}
  }
  return JSON.parse(JSON.stringify(object))
}

export default (state, action) => {
  const newState = clone(state)

  if (ServerEventsReducer.getEvents().indexOf(action.type) >= 0) {
    return ServerEventsReducer.reduceEvent(newState, action)

  } else if (ClientEventsReducer.getEvents().indexOf(action.type) >= 0) {
    return ClientEventsReducer.reduceEvent(newState, action)

  } else if (!state) {
    return {}

  } else {
    throw new Error(`Unknown action type ${action.type}`)
  }
}