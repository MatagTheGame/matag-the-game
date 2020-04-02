import React, {Component} from 'react'
import './login.scss'
import {connect} from 'react-redux'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'
import ApiClient from '../../Common/ApiClient'
import Loader from '../../Common/Loader'
import FieldValidation from '../../Common/FieldValidation'

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
    if (!FieldValidation.isValidateEmail(email)) {
      this.props.loginErrorMessage('Email is invalid.')
      return
    }

    if (password.length < 4) {
      this.props.loginErrorMessage('Password is invalid.')
      return
    }

    const request = {
      email: email,
      password: password
    }

    this.props.loginLoading()
    ApiClient.postNoRedirect('/auth/login', request)
      .then(response => !response.ok ? 'Email or password are not correct.' : null)
      .then(message => this.props.loginErrorMessage(message))
  }

  handleLogin(event) {
    event.preventDefault()
    this.login(this.state.email, this.state.password)
  }

  handleLoginAsGuest() {
    this.login('guest@matag.com', 'password')
  }

  displayErrorMessage() {
    if (this.props.errorMessage) {
      return (
        <div className='grid grid-label-value'>
          <div/>
          <div className='error'>{this.props.errorMessage}</div>
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
    return (
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
          { this.displayErrorMessage() }
          <div className='grid three-columns'>
            <div/>
            <div className='login-buttons'>
              <input type='submit' value='Login'/>
              <div className='or'>or</div>
              <input type='button' value='Login as Guest' onClick={this.handleLoginAsGuest}/>
            </div>
            { this.displayLoader() }
          </div>
        </form>
      </div>
    )
  }
}

const loginLoading = () => {
  return {
    type: 'LOGIN_LOADING'
  }
}

const loginErrorMessage = (message) => {
  return {
    type: 'LOGIN_ERROR_MESSAGE',
    value: message
  }
}

const mapStateToProps = state => {
  return {
    loading: get(state, 'login.loading', false),
    errorMessage: get(state, 'login.message', null),
  }
}

const mapDispatchToProps = dispatch => {
  return {
    loginLoading: bindActionCreators(loginLoading, dispatch),
    loginErrorMessage: bindActionCreators(loginErrorMessage, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Login)