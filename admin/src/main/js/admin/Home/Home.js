import React, {Component} from 'react'
import {connect} from 'react-redux'
import Login from 'admin/Auth/Login/Login'
import AuthHelper from 'admin/Auth/AuthHelper'
import Play from 'admin/Play/Play'
import Stats from './Stats/Stats'
import Intro from './Intro/Intro'

class Home extends Component {
  displayMainAction() {
    if (this.props.isLoggedIn) {
      return <Play/>
    } else {
      return <Login />
    }
  }

  render() {
    return (
      <section>
        <h2>Home</h2>
        <Intro/>
        <Stats/>
        { this.displayMainAction() }
      </section>
    )
  }
}

const mapStateToProps = state => {
  return {
    isLoggedIn: AuthHelper.isLoggedIn(state)
  }
}

const mapDispatchToProps = dispatch => {
  return {}
}

export default connect(mapStateToProps, mapDispatchToProps)(Home)