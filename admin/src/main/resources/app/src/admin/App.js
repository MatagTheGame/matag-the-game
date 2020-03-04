import React, {Component} from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'


class App extends Component {
  render() {
    return (
      <div>Mtg Admin</div>
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
