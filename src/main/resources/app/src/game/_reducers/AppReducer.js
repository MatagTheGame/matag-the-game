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

  if (ServerEventsReducer.getEvents().find(event => action.type === event)) {
    return ServerEventsReducer.reduceEvent(newState, action)

  } else if (ClientEventsReducer.getEvents().find(event => action.type === event)) {
    return ClientEventsReducer.reduceEvent(newState, action)

  } else if (!state) {
    return {}

  } else {
    throw new Error('Unknown action type ' + action.type)
  }
}