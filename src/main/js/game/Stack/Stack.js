import React, {Component} from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import Card from 'game/Card/Card'
import StackUtils from './StackUtils'
import {TriggeredAbility} from './TriggeredAbility'
import './stack.scss'

class Stack extends Component {

  static renderStackItem(cardInstance, i) {
    const style = {transform: 'translateY(-150px) translateX(' + (i * 50) + 'px) translateZ(' + (i * 150) + 'px)'}
    if (StackUtils.isACastedCard(cardInstance)) {
      return (
        <span key={cardInstance.id} style={style}>
          <Card cardInstance={cardInstance} area='stack'/>
        </span>
      )

    } else {
      return (
        <span key={cardInstance.id} style={style}>
          <TriggeredAbility cardInstance={cardInstance} />
        </span>
      )
    }
  }

  renderStack() {
    return this.props.stack.map((cardInstance, index) => Stack.renderStackItem(cardInstance, index))
  }

  render() {
    return (
      <div id='stack'>
        {this.renderStack()}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    stack: get(state, 'stack', [])
  }
}

Stack.propTypes = {
  stack: PropTypes.array.isRequired
}

export default connect(mapStateToProps)(Stack)