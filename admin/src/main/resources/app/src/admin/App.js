import React, {Component} from 'react'
import {BrowserRouter as Router, Switch, Route, Link} from "react-router-dom";
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'


class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <nav>
            <ul>
              <li>
                <Link to="/ui/admin">Home</Link>
              </li>
              <li>
                <Link to="/ui/admin/login">Login</Link>
              </li>
              <li>
                <Link to="/ui/admin/users">Users</Link>
              </li>
            </ul>
          </nav>

          <Switch>
            <Route path="/ui/admin/login">
              <div>
                <h2>Login</h2>
                <form className="mtg-form">
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
            </Route>
            <Route path="/ui/admin/users">
              <div>Users</div>
            </Route>
            <Route path="/ui/admin">
              <div>Home</div>
            </Route>
          </Switch>
        </div>
      </Router>
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
