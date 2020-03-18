import React from 'react'
import {render} from 'react-dom'
import {Provider} from 'react-redux'
import {createStore} from 'redux'
import {composeWithDevTools} from 'redux-devtools-extension'
import AdminApp from './admin/AdminApp'
import AppReducer from './admin/_reducers/AppReducer'

const store = createStore(AppReducer, composeWithDevTools())

render(
  <Provider store={store}>
    <AdminApp/>
  </Provider>,
  document.getElementById('app')
)
