import React, {Component} from 'react'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import Loader from 'admin/Common/Loader'
import ApiClient from 'admin/utils/ApiClient'

class Stats extends Component {
  componentDidMount() {
    this.props.loadStats()
    ApiClient.get('/stats').then(this.props.statsLoaded)
  }

  stats() {
    if (this.props.loading) {
      return <Loader/>
    } else {
      return (
        <ul>
          <li><span>Total Users: </span><span>{this.props.stats.totalUsers}</span></li>
          <li><span>Online Users: </span><span>{this.props.stats.onlineUsers}</span></li>
          <li><span>Total Cards: </span><span>{this.props.stats.totalCards}</span></li>
          <li><span>Total Sets: </span><span>{this.props.stats.totalSets} (from Ixalan)</span></li>
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
    type: 'STATS_LOADING'
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
    stats: get(state, 'stats.value', {})
  }
}

const mapDispatchToProps = dispatch => {
  return {
    loadStats: bindActionCreators(loadStats, dispatch),
    statsLoaded: bindActionCreators(statsLoaded, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Stats)
