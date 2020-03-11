import React, {Component} from 'react'

export default class Login extends Component {
  render() {
    return (
      <div>
        <h2>Login</h2>
        <form className="matag-form">
          <div className="grid grid-label-value">
            <label htmlFor='username'>Username: </label><input type='text' name='username'/>
          </div>
          <div className="grid grid-label-value">
            <label htmlFor='password'>Password: </label><input type='password' name='password'/>
          </div>
          <div className="grid">
            <input type='submit' value='Login'/>
          </div>
        </form>
      </div>
    )
  }
}
