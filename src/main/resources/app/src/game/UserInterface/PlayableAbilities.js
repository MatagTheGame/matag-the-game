import React, {Component} from 'react'
import {connect} from 'react-redux'
import PropTypes from 'prop-types'

class PlayableAbilities extends Component {
  constructor(props) {
    super(props)
  }

  render() {
    return (
      <div id="playable-abilities">
        <ul>
          <li>TAP: add 1 WHITE mana</li>
          <li>TAP: add 1 BLUE mana</li>
        </ul>
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

PlayableAbilities.propTypes = {

}


export default connect(mapStateToProps, mapDispatchToProps)(PlayableAbilities)