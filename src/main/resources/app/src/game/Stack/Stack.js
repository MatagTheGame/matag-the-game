import React, {Component} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import Card from '../Card/Card'
import PropTypes from 'prop-types'
import StackUtils from './StackUtils'
import {TriggeredAbility} from './TriggeredAbility'

class Stack extends Component {

  static renderStackItem(cardInstance, i) {
    const style = {transform: 'translateY(-150px) translateX(' + (i * 50) + 'px) translateZ(' + (i * 150) + 'px)'}
    console.log(style)
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
    return this.props.stack.items.map((cardInstance, index) => Stack.renderStackItem(cardInstance, index))
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