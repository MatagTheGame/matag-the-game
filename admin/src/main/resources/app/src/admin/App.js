import React, {Component} from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'


class App extends Component {
  handleSubmit() {
    console.log('Login')
  }

  render() {
    return (
      <div>
        <h2>Login</h2>
        <form className="mtg-form" onSubmit={this.handleSubmit()}>
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

const mapStateToProps = () => {
  return {}
}

const mapDispatchToProps = dispatch => {
  return {
    receive: bindActionCreators((event) => event, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(App)
