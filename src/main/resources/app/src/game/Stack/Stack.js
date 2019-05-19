import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import Card from '../Card/Card'
import PropTypes from 'prop-types'
import StackUtils from './StackUtils'
import {TriggeredAbility} from './TriggeredAbility'

class Stack extends PureComponent {

  static renderCastedCard(cardInstance) {
    return (
      <span key={cardInstance.id}>
        <Card cardInstance={cardInstance} area='stack'/>
      </span>
    )
  }

  static renderTriggeredStackAbility(cardInstance) {
    return (
      <span key={cardInstance.id}>
        <TriggeredAbility cardInstance={cardInstance} />
      </span>
    )
  }

  renderStackItem(cardInstance) {
    if (StackUtils.isACastedCard(cardInstance)) {
      return Stack.renderCastedCard(cardInstance)
    } else {
      return Stack.renderTriggeredStackAbility(cardInstance)
    }
  }

  renderStack() {
    return this.props.stack.items.map((cardInstance) => this.renderStackItem(cardInstance))
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
    stack: get(state, 'stack', {items: []})
  }
}

Stack.propTypes = {
  stack: PropTypes.object.isRequired
}

export default connect(mapStateToProps)(Stack)