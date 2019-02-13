import {combineReducers} from 'redux'
import stompReducer from './stompReducer'

export default combineReducers({
  stomp: stompReducer,
})