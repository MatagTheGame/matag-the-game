import React, {Component} from 'react'
import {Link} from 'react-router-dom'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import get from 'lodash/get'
import Login from './Login/Login'
import Loader from '../Common/Loader'

class Home extends Component {
  componentDidMount() {
    this.props.loadStats()
  }

  stats() {
    if (this.props.loadStats) {
      return <Loader/>
    }
  }

  render() {
    return (
      <div>
        <h1>MATAG Home</h1>

        <section id='intro'>
          <p>
            Welcome to Matag, a web based version of <strong>Magic: The Gathering</strong> where you can play using just your browser.
          </p>
          <p>
            The game is open source and you can find the code and contribute or contact the creators at <a href='https://github.com/antonioalonzi/matag' target='_blank'>matag</a> github.<br/>
            (Please note that we are not affiliated in any way with the MTG creators, nor we claim any copyright over their game or art assets.)
          </p>
        </section>

        <section id='stats'>
          { this.stats() }
        </section>

        <div><Link to="/ui/admin/decks">Decks</Link></div>

        <Login/>
      </div>
    )
  }
}

const loadStats = () => {
  return {
    type: 'LOAD_STATS'
  }
}

const mapStateToProps = state => {
  return {
    loading: get(state, 'stats.loading', false)
  }
}

const mapDispatchToProps = dispatch => {
  return {
    loadStats: bindActionCreators(loadStats, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Home)