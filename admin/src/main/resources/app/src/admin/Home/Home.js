import React, {Component} from 'react'
import {Link} from 'react-router-dom'
import Login from './Login/Login'

export default class Home extends Component {
  render() {
    return (
      <div>
        <h1>MATAG Home</h1>

        <section id='intro'>
          <p>
            Welcome to Matag, a web based version of <strong>Magic: The Gathering</strong> where you can play using just your browser.
          </p>
          <p>
            The game is open source and you can find the code and contribute or contact the creators at <a href='https://github.com/antonioalonzi/matag' target='_blank'>matag</a> github.<br/>
            (Please note that we are not affiliated in any way with the MTG creators, nor we claim any copyright over their game or art assets.)
          </p>
        </section>

        <section id='stats'>

        </section>

        <div><Link to="/ui/admin/decks">Decks</Link></div>

        <Login/>
      </div>
    )
  }
}
