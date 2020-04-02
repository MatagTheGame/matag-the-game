import React from 'react'
import {render} from 'react-dom'
import {Provider} from 'react-redux'
import {createStore} from 'redux'
import {composeWithDevTools} from 'redux-devtools-extension'
import GameApp from './game/GameApp'
import AppReducer from './game/_reducers/AppReducer'

const store = createStore(AppReducer, composeWithDevTools())

render(
  <Provider store={store}>
    <GameApp/>
  </Provider>,
  document.getElementById('app')
)
