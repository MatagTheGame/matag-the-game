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
        <form onSubmit={this.handleSubmit()}>
          <label>Username</label>
          <input type='text' name='username'/>
          <label>Password</label>
          <input type='password' name='password'/>
          <input type='submit'/>
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
