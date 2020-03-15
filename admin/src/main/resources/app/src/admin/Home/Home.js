import React, {Component} from 'react'
import {Link} from 'react-router-dom'
import Login from './Login/Login'
import Stats from './Stats/Stats'
import Intro from './Intro/Intro'

export default class Home extends Component {
  render() {
    return (
      <div>
        <h1>MATAG Home</h1>
        <Intro/>
        <Stats/>
        <div><Link to="/ui/admin/decks">Decks</Link></div>
        <Login/>
      </div>
    )
  }
}
