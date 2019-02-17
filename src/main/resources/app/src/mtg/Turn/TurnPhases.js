import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'

class TurnPhases extends PureComponent {
  render() {
    console.log('phasesConfig: ', this.props.phases)
    return (
      <div id='turn-phases'>
        {this.props.phases.map((phase) =>
          <span className={phase.status.toLowerCase()}>{phase.name}</span>)}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    phases: get(state, 'turn.phasesConfig', []),
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(TurnPhases)