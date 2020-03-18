import React, {Component} from 'react'
import {BrowserRouter as Router, Switch, Route, Link} from 'react-router-dom'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import Home from './Home/Home'
import './admin.scss'
import Header from './Header/Header'

// Copy layout from https://www.wix.com/website-template/view/html/1791?siteId=97d5d35e-d343-4d48-860f-22d22a8b6a6d&metaSiteId=a9f72a56-c68c-4a21-89d9-e8cfeb881d10&originUrl=https%3A%2F%2Fwww.wix.com%2Fwebsite%2Ftemplates

class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <Header/>
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
