import React, {Component} from 'react'
import {Link} from 'react-router-dom'
import get from 'lodash/get'
import {connect} from 'react-redux'
import Logout from 'admin/Auth/Logout/Logout'
import './header.scss'

class Header extends Component {
  displayMenu() {
    if (!this.props.profile.username) {
      return (
        <div id='menu-bar'>
          <nav>
            <Link to="/ui/admin">Home</Link>
          </nav>
        </div>
      )

    } else {
      return (
        <div id='menu-bar'>
          <nav>
            <Link to="/ui/admin">Home</Link>
          </nav>
          <nav>
            <Link to="/ui/admin/decks">Decks</Link>
          </nav>
          <nav>
            <Logout/>
          </nav>
          <nav className='welcome'>
            <span>Welcome {this.props.profile.username}</span>
          </nav>
        </div>
      )
    }
  }

  render() {
    return (
      <header>
        <div id='logo'>
          <h1>MATAG</h1>
        </div>
        {this.displayMenu()}
      </header>
    )
  }
}

const mapStateToProps = state => {
  return {
    profile: get(state, 'session.profile', {}),
  }
}

const mapDispatchToProps = dispatch => {
  return {}
}

export default connect(mapStateToProps, mapDispatchToProps)(Header)