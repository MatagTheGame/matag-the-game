import React, {Component} from 'react'
import './login.scss'
import {connect} from 'react-redux'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'

class Login extends Component {
  constructor(props) {
    super(props)
    this.state = {
      username: '',
      password: ''
    }
    this.handleChangeUsername = this.handleChangeUsername.bind(this)
    this.handleChangePassword = this.handleChangePassword.bind(this)
    this.login = this.login.bind(this)
  }

  handleChangeUsername(event) {
    this.setState({username: event.target.value})
  }

  handleChangePassword(event) {
    this.setState({password: event.target.value})
  }

  login(event) {
    event.preventDefault()
    const request = {
      username: this.state.username,
      password: this.state.password
    }

    this.props.loginLoading()
    fetch('/auth/login', {method: 'POST', body: JSON.stringify(request)})
      .then((response) => response.json())
      .then((data) => this.props.loginResponse(data))
  }

  render() {
    return (
      <div id='login-container'>
        <h2>Login</h2>
        <form className='matag-form' onSubmit={this.login}>
          <div className='grid grid-label-value'>
            <label htmlFor='username'>Username: </label>
            <input type='text' name='username' value={this.state.username} onChange={this.handleChangeUsername}/>
          </div>
          <div className='grid grid-label-value'>
            <label htmlFor='password'>Password: </label>
            <input type='password' name='password' value={this.state.password} onChange={this.handleChangePassword}/>
          </div>
          <div className='grid'>
            <div className='login-buttons'>
              <input type='submit' value='Login'/>
              <div className='or'>or</div>
              <input type='submit' value='Login as Guest'/>
            </div>
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

const loginResponse = (data) => {
  return {
    type: 'LOGIN_RESPONSE',
    value: data
  }
}

const mapStateToProps = state => {
  return {
    loading: get(state, 'login.loading', false),
  }
}

const mapDispatchToProps = dispatch => {
  return {
    loginLoading: bindActionCreators(loginLoading, dispatch),
    loginResponse: bindActionCreators(loginResponse, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Login)