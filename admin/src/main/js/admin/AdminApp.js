import React, {Component} from 'react'
import {BrowserRouter as Router, Link, Route, Switch} from 'react-router-dom'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import Home from './Home/Home'
import './admin.scss'
import Header from './Header/Header'
import ProfileUtils from './Profile/ProfileUtils'
import get from 'lodash/get'
import Loader from './Common/Loader'
import AuthHelper from './Auth/AuthHelper'

// Copy layout from https://www.wix.com/website-template/view/html/1791?siteId=97d5d35e-d343-4d48-860f-22d22a8b6a6d&metaSiteId=a9f72a56-c68c-4a21-89d9-e8cfeb881d10&originUrl=https%3A%2F%2Fwww.wix.com%2Fwebsite%2Ftemplates

class AdminApp extends Component {
  componentDidMount() {
    if (AuthHelper.hasToken()) {
      ProfileUtils.getProfile().then(this.props.profileLoaded)
    } else {
      this.props.profileLoaded({})
    }
  }

  render() {
    if (this.props.loading) {
      return <div className='fullscreen-loader'><Loader/></div>

    } else {
      return (
          <Router>
            <div>
              <Header/>
              <Switch>
                <Route path="/ui/admin/decks">
                  <h1>Decks</h1>
                  <div><Link to="/ui/admin">Home</Link></div>
                </Route>
                <Route path="/ui/admin">
                  <Home/>
                </Route>
              </Switch>
            </div>
          </Router>
      )
    }
  }
}

const profileLoaded = (profile) => {
  return {
    type: 'PROFILE_LOADED',
    value: profile
  }
}

const mapStateToProps = state => {
  return {
    loading: get(state, 'session.loading', true),
  }
}

const mapDispatchToProps = dispatch => {
  return {
    profileLoaded: bindActionCreators(profileLoaded, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(AdminApp)
