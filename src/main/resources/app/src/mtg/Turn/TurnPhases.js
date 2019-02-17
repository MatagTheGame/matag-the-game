import React, {PureComponent} from 'react';
import {connect} from 'react-redux'

class TurnPhases extends PureComponent {
  render() {
    return (
      <div id='turn-phases'>
        <span className={'disabled'}>UP</span>
        <span className={'disabled'}>DR</span>
        <span className={'enabled'}>M1</span>
        <span className={'disabled'}>BC</span>
        <span className={'enabled'}>DA</span>
        <span className={'enabled'}>DB</span>
        <span className={'disabled'}>FS</span>
        <span className={'disabled'}>CD</span>
        <span className={'disabled'}>EC</span>
        <span className={'enabled'}>M2</span>
        <span className={'disabled'}>ET</span>
        <span className={'disabled'}>CL</span>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(TurnPhases)