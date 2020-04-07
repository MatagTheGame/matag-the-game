import React, {Component} from 'react'
import {Route, Router, Switch} from 'react-router-dom'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import get from 'lodash/get'
import Login from 'admin/Auth/Login/Login'
import Loader from 'admin/Common/Loader'
import Decks from 'admin/Decks/Decks'
import Header from 'admin/Header/Header'
import Home from 'admin/Home/Home'
import ProfileUtils from 'admin/Profile/ProfileUtils'
import ApiClient from 'admin/utils/ApiClient'
import history from 'admin/utils/history'
import Play from './Play/Play'
import './admin.scss'

// Copy layout from https://www.wix.com/website-template/view/html/1791?siteId=97d5d35e-d343-4d48-860f-22d22a8b6a6d&metaSiteId=a9f72a56-c68c-4a21-89d9-e8cfeb881d10&originUrl=https%3A%2F%2Fwww.wix.com%2Fwebsite%2Ftemplates

class AdminApp extends Component {
  componentDidMount() {
    ApiClient.get('/config').then(this.props.configLoaded)
    ProfileUtils.getProfile().then(this.props.profileLoaded)
  }

  render() {
    if (this.props.loading) {
      return <div className='fullscreen-loader'><Loader/></div>

    } else {
      return (
          <Router history={history}>
            <div>
              <Header/>
              <Switch>
                <Route path="/ui/admin" exact>
                  <div className='page with-margin'><Home/></div>
                </Route>
                <Route path="/ui/admin/login">
                  <div className='page with-margin'><Login/></div>
                </Route>
                <Route path="/ui/admin/decks">
                  <div className='page'><Decks/></div>
                </Route>
                <Route path="/ui/admin/play">
                  <div className='page with-margin'><Play/></div>
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

const configLoaded = (config) => {
  return {
    type: 'CONFIG_LOADED',
    value: config
  }
}

const mapStateToProps = state => {
  return {
    loading: get(state, 'session.loading', true),
  }
}

const mapDispatchToProps = dispatch => {
  return {
    configLoaded: bindActionCreators(configLoaded, dispatch),
    profileLoaded: bindActionCreators(profileLoaded, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(AdminApp)
