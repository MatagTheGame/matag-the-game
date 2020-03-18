import React, {Component} from 'react'
import './header.scss'

export default class Header extends Component {
  render() {
    return (
      <header>
        <div className='header-container'>
          <div id='logo'>
            <p>MATAG</p>
          </div>
          <nav id='menu'>
            <p>MATAG</p>
          </nav>
        </div>
      </header>
    )
  }
}
