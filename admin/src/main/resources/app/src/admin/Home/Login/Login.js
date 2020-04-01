import React, {Component} from 'react'
import './login.scss'

export default class Login extends Component {
  render() {
    return (
      <div id='login-container'>
        <h2>Login</h2>
        <form className='matag-form'>
          <div className='grid grid-label-value'>
            <label htmlFor='username'>Username: </label><input type='text' name='username'/>
          </div>
          <div className='grid grid-label-value'>
            <label htmlFor='password'>Password: </label><input type='password' name='password'/>
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
