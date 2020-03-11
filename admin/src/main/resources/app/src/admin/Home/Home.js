import React, {Component} from 'react'
import {Link} from 'react-router-dom'
import Login from './Login/Login'

export default class Home extends Component {
  render() {
    return (
      <div>
        <h1>Home</h1>
        <div>
          Welcome to Matag a web base version where to play Magic: The Gathering online with just your browser.
        </div>
        <div><Link to="/ui/admin/decks">Decks</Link></div>
        <Login/>
      </div>
    )
  }
}
