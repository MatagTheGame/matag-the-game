import React, {Component} from 'react'
import Loader from '../../Common/Loader'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'

class Stats extends Component {
  componentDidMount() {
    this.props.loadStats()
    fetch('/stats')
      .then((response) => {
        return response.json()
      })
      .then((data) => {
        this.props.statsLoaded(data)
      })
  }

  stats() {
    if (this.props.loading) {
      return <Loader/>
    } else {
      return (
        <ul>
          <li><span>Total Users: </span><span>{this.props.totalUsers}</span></li>
          <li><span>Online Users: </span><span>{this.props.onlineUsers}</span></li>
          <li><span>Total Cards: </span><span>{this.props.totalCards}</span></li>
        </ul>
      )
    }
  }

  render() {
    return (
      <section id='stats'>
        { this.stats() }
      </section>
    )
  }
}

const loadStats = () => {
  return {
    type: 'LOAD_STATS'
  }
}

const statsLoaded = (stats) => {
  return {
    type: 'STATS_LOADED',
    value: stats
  }
}

const mapStateToProps = state => {
  return {
    loading: get(state, 'stats.loading', false),
    totalUsers: get(state, 'stats.value.totalUsers', 0),
    onlineUsers: get(state, 'stats.value.onlineUsers', 0),
    totalCards: get(state, 'stats.value.totalCards', 0)
  }
}

const mapDispatchToProps = dispatch => {
  return {
    loadStats: bindActionCreators(loadStats, dispatch),
    statsLoaded: bindActionCreators(statsLoaded, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Stats)
