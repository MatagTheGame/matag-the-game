import React, {Component} from 'react'
import Loader from '../../Common/Loader'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'

class Stats extends Component {
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

export default connect(mapStateToProps, mapDispatchToProps)(Stats)
