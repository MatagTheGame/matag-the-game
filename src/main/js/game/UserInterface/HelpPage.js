import React, {Component, Fragment} from 'react'
import PropTypes from 'prop-types'
import {connect} from 'react-redux'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'

class HelpPage extends Component {
  constructor(props) {
    super(props)
    this.handleEscape = this.handleEscape.bind(this)
  }

  handleEscape(event) {
    if (event.key === 'Escape') {
      this.props.closeHelpPage()
    }
  }

  componentDidMount() {
    document.addEventListener('keydown', this.handleEscape)
  }

  componentWillUnmount() {
    document.removeEventListener('keydown', this.handleEscape)
  }

  renderCloseButton() {
    return <i id='popup-close' onClick={this.props.closeHelpPage} aria-hidden='true'>X</i>
  }

  static renderContent() {
    return (
      <span>
        <h2>Help</h2>
        <h4>This is an online version of the card game <i>Magic the Gathering</i>.
          If you have never played before, please refer to the
        <a target="_blank" href="https://magic.wizards.com/en/magic-gameplay"><i>official game rules</i></a>.
        </h4>
        <h5>The following are rules specific to this version:</h5>
        <ul>
          <li>Mana needs to be tapped before to click the card to play.</li>
          <li>Turn phases are on the right, the light green is the current phase and the little circle next to it indicate the player with priority.</li>
          <li>The underlined player is the player that has the current turn.</li>
          <li>The status bar at the bottom tells you what to do.</li>
          <li>If a card/player is clicked and no action can be done at the moment probably nothing will happen.</li>
        </ul>
        <h5>The following are available navigation shortcuts:</h5>
        <ul>
          <li>SPACE - continue</li>
          <li>SCROLL - on a card to maximize/minimize it</li>
        </ul>
      </span>
    )
  }

  render() {
    if (this.props.helpOpen) {
      return (
        <Fragment>
          <div id='modal-container' />
          <div id='popup'>
            { this.renderCloseButton() }
            { HelpPage.renderContent() }
          </div>
        </Fragment>
      )
    } else {
      return <Fragment />
    }
  }
}

const closeHelpPageAction = () => {
  return {
    type: 'CLOSE_HELP_PAGE'
  }
}

const mapStateToProps = state => {
  return {
    helpOpen: get(state, 'helpOpen', false)
  }
}

const mapDispatchToProps = dispatch => {
  return {
    closeHelpPage: bindActionCreators(closeHelpPageAction, dispatch)
  }
}

HelpPage.propTypes = {
  helpOpen: PropTypes.bool,
  closeHelpPage: PropTypes.func.isRequired
}

export default connect(mapStateToProps, mapDispatchToProps)(HelpPage)