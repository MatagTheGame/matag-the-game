import React from 'react'
import {render} from 'react-dom'
import {Provider} from 'react-redux'
import {createStore} from 'redux';
import {composeWithDevTools} from 'redux-devtools-extension';
import App from './mtg/App'
import AppReducer from './mtg/AppReducer'

const store = createStore(AppReducer, composeWithDevTools());

render(
  <Provider store={store}>
    <App/>
  </Provider>,
  document.getElementById('app')
)
