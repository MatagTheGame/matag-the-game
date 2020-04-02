import React, {useState} from 'react'
import './login.scss'
import {connect} from 'react-redux'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'

function Login(props) {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')

  const login = (event) => {
    event.preventDefault()
    const request = {
      'username': username,
      'password': password
    }

    props.loginLoading()
    fetch('/auth/login', {method: 'POST', body: JSON.stringify(request)})
      .then((response) => response.json())
      .then((data) => props.loginResponse(data))
  }

  return (
    <div id='login-container' onSubmit={login}>
      <h2>Login</h2>
      <form className='matag-form'>
        <div className='grid grid-label-value'>
          <label htmlFor='username'>Username: </label>
          <input type='text' name='username' value={username} onChange={(e) => setUsername(e.target.value)}/>
        </div>
        <div className='grid grid-label-value'>
          <label htmlFor='password'>Password: </label>
          <input type='password' name='password' value={password} onChange={(e) => setPassword(e.target.value)}/>
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
