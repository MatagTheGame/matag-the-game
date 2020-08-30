import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import './userInputs.scss'

class UserInput extends Component {
  render() {
    return <li onClick={this.props.onClick} title={this.props.title}>
      {this.props.children}
    </li>
  }
}

UserInput.propTypes = {
  onClick: PropTypes.func.isRequired,
  title: PropTypes.string.isRequired
}



class UserInputs extends Component {
  constructor(props) {
    super(props)
    this.closeUserInputsOverlay = this.closeUserInputsOverlay.bind(this)
  }

  closeUserInputsOverlay() {
    this.props.closeUserInputsOverlay()
  }

  render() {
    if (this.props.userOptions.length > 0) {
      return (
        <div id='user-inputs-overlay' onClick={this.closeUserInputsOverlay}>
          <div id='user-inputs' style={{left: this.props.position.x + 'px', top: (this.props.position.y - 150) + 'px'}}>
            <ul>{this.props.userOptions.map((userOption, index) =>
                <UserInput key={index} title={userOption.title} onClick={() => {userOption.func(index)}}>{userOption.component}</UserInput>)}
            </ul>
          </div>
        </div>
      )
    } else {
      return <></>
    }
  }
}

const closeUserInputsAction = () => {
  return {
    type: 'CLOSE_USER_INPUTS_CLICK'
  }
}

const mapStateToProps = state => {
  return {
    position: get(state, 'userInterface.userInputs.position', {x: -1, y: -1})
  }
}

const mapDispatchToProps = dispatch => {
  return {
    closeUserInputsOverlay: bindActionCreators(closeUserInputsAction, dispatch),
  }
}

UserInputs.propTypes = {
  userOptions: PropTypes.array.isRequired,
  position: PropTypes.object.isRequired
}

export default connect(mapStateToProps, mapDispatchToProps)(UserInputs)
