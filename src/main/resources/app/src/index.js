import React from 'react'
import {render} from 'react-dom'
import {Provider} from 'react-redux'
import {createStore} from 'redux'
import allReducers from './mtg/reducers/allReducers'
import App from './mtg/components/App'

const store = createStore(allReducers)

render(
  <Provider store={store}>
    <App/>
  </Provider>,
  document.getElementById('app')
)
