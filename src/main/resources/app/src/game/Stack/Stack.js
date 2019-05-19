import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import Card from '../Card/Card'
import PropTypes from 'prop-types'

class Stack extends PureComponent {

  renderStack() {
    return this.props.stack.items.map((cardInstance) =>
      <span key={cardInstance.id}>
        <Card cardInstance={cardInstance} area='stack' />
      </span>
    )
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