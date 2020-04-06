import React, {Component} from 'react'
import {Link} from 'react-router-dom'
import AuthHelper from '../AuthHelper'
import ApiClient from '../../utils/ApiClient'

export default class Login extends Component {
  logout() {
    AuthHelper.removeToken()
    ApiClient.getNoJson('/auth/logout')
      .then(() => window.location.href = '/')
  }

  render() {
    return (
        <Link to='#' onClick={this.logout}>Logout</Link>
    )
  }
}
