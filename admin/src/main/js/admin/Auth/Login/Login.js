import React, {Component} from 'react'
import './login.scss'
import {connect} from 'react-redux'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'
import Loader from 'admin/Common/Loader'
import ApiClient from 'admin/utils/ApiClient'
import history from 'admin/utils/history'
import AuthHelper from '../AuthHelper'

class Login extends Component {
  constructor(props) {
    super(props)
    this.state = {
      email: '',
      password: ''
    }
    this.handleChangeEmail = this.handleChangeEmail.bind(this)
    this.handleChangePassword = this.handleChangePassword.bind(this)
    this.handleLogin = this.handleLogin.bind(this)
    this.handleLoginAsGuest = this.handleLoginAsGuest.bind(this)
  }

  handleChangeEmail(event) {
    this.setState({email: event.target.value})
  }

  handleChangePassword(event) {
    this.setState({password: event.target.value})
  }

  login(email, password) {
    const request = {
      email: email,
      password: password
    }

    this.props.loginLoading()
    ApiClient.postNoJson('/auth/login', request)
      .then(response => response.json())
      .then(response => this.props.loginResponse(response))
  }

  handleLogin(event) {
    event.preventDefault()
    this.login(this.state.email, this.state.password)
  }

  handleLoginAsGuest() {
    this.login('guest@matag.com', 'password')
  }

  displayError() {
    if (this.props.error) {
      return (
        <div className='grid grid-label-value'>
          <div/>
          <div className='error'>{this.props.error}</div>
        </div>
      )
    }
  }

  displayLoader() {
    if (this.props.loading) {
      return (
        <div className='spinner-container'>
          <Loader/>
        </div>
      )
    }
  }

  render() {
    if (this.props.isLoggedIn) {
      history.push("/ui/admin");
    }

    return (
      <section>
        <div id='login-container'>
          <h2>Login</h2>
          <form className='matag-form' onSubmit={this.handleLogin}>
            <div className='grid grid-label-value'>
              <label htmlFor='email'>Email: </label>
              <input type='text' name='email' value={this.state.email} onChange={this.handleChangeEmail}/>
            </div>
            <div className='grid grid-label-value'>
              <label htmlFor='password'>Password: </label>
              <input type='password' name='password' value={this.state.password} onChange={this.handleChangePassword}/>
            </div>
            {this.displayError()}
            <div className='grid three-columns'>
              <div/>
              <div className='login-buttons'>
                <input type='submit' value='Login'/>
                <div className='or'>or</div>
                <input type='button' value='Login as Guest' onClick={this.handleLoginAsGuest}/>
              </div>
              {this.displayLoader()}
            </div>
          </form>
        </div>
      </section>
    )
  }
}

const loginLoading = () => {
  return {
    type: 'LOGIN_LOADING'
  }
}

const loginResponse = (response) => {
  return {
    type: 'LOGIN_RESPONSE',
    value: response
  }
}

const mapStateToProps = state => {
  return {
    isLoggedIn: AuthHelper.isLoggedIn(state),
    loading: get(state, 'login.loading', false),
    error: get(state, 'login.error', null),
  }
}

const mapDispatchToProps = dispatch => {
  return {
    loginLoading: bindActionCreators(loginLoading, dispatch),
    loginResponse: bindActionCreators(loginResponse, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Login)