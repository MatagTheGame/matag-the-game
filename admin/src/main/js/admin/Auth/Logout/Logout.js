import React, {Component} from 'react'
import {Link} from 'react-router-dom'
import AuthHelper from 'admin/Auth/AuthHelper'
import ApiClient from 'admin/utils/ApiClient'

export default class Logout extends Component {
  logout() {
    ApiClient.getNoJson('/auth/logout')
      .then(() => {
        AuthHelper.removeToken()
        window.location.href = '/'
      })
  }

  render() {
    return (
        <Link to='#' onClick={this.logout}>Logout</Link>
    )
  }
}
