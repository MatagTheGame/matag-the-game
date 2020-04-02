import React, {useEffect} from 'react'
import Loader from '../../Common/Loader'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'

function Stats(props) {
  useEffect(() => {
    props.loadStats()
    fetch('/stats')
      .then((response) => response.json())
      .then((data) => props.statsLoaded(data))
  }, [])

  const stats = () => {
    if (props.loading) {
      return <Loader/>
    } else {
      return (
        <ul>
          <li><span>Total Users: </span><span>{props.totalUsers}</span></li>
          <li><span>Online Users: </span><span>{props.onlineUsers}</span></li>
          <li><span>Total Cards: </span><span>{props.totalCards}</span></li>
        </ul>
      )
    }
  }

  return (
    <section id='stats'>
      { stats() }
    </section>
  )
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
