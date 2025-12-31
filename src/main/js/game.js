import React from 'react'
import {createRoot} from 'react-dom/client'
import {Provider} from 'react-redux'
import {createStore} from 'redux'
import {composeWithDevTools} from 'redux-devtools-extension'
import GameApp from './game/GameApp'
import AppReducer from './game/_reducers/AppReducer'

const store = createStore(AppReducer, composeWithDevTools())

const root = createRoot(document.getElementById('app'))
root.render(<Provider store={store}><GameApp/></Provider>)
