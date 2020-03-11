import React, {Component} from 'react'
import {BrowserRouter as Router, Switch, Route, Link} from 'react-router-dom'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import Home from './Home/Home'


class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <Switch>
            <Route path="/ui/admin/decks">
              <h1>Decks</h1>
              <div><Link to="/ui/admin">Home</Link></div>
            </Route>
            <Route path="/ui/admin">
              <Home/>
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
