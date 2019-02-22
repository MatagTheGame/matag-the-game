import ServerEventsReducer from './ServerEventsReducer'
import ClientEventsReducer from './ClientEventsReducer'

export default (state, action) => {
  const newState = Object.assign({}, state)

  if (ServerEventsReducer.getEvents().find(event => action.type === event)) {
    return ServerEventsReducer.reduceEvent(state, newState, action)

  } else if (ClientEventsReducer.getEvents().find(event => action.type === event)) {
    return ClientEventsReducer.reduceEvent(state, newState, action)

  } else {
    throw new Error('Unknown action type ' + action.type)
  }
}