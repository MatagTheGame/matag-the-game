import React, {Component} from 'react'
import './header.scss'
import {Link} from 'react-router-dom'

export default class Header extends Component {
  render() {
    return (
      <header>
        <h1>MATAG</h1>
        <nav id='menu'>
          <Link to="/ui/admin/decks">Decks</Link>
        </nav>
      </header>
    )
  }
}
