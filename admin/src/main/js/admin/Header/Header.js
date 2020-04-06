import React, {Component} from 'react'
import './header.scss'
import {Link} from 'react-router-dom'
import get from 'lodash/get'
import {connect} from 'react-redux'
import Logout from 'admin/Auth/Logout/Logout'

class Header extends Component {
  displayMenu() {
    if (!this.props.profile.username) {
      return <></>

    } else {
      return (
          <>
            <nav id='menu'>
              <Link to="/ui/admin/decks">Decks</Link>
            </nav>
            <nav id='menu'>
              <Logout/>
            </nav>
          </>
      )
    }
  }

  render() {
    return (
      <header>
        <h1>MATAG</h1>
        { this.displayMenu() }
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