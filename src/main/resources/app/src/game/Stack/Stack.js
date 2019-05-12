import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import Card from '../Card/Card'
import PropTypes from 'prop-types'

class Stack extends PureComponent {

  renderCards() {
    return this.props.stack.map((cardInstance) =>
      <span key={cardInstance.id}>
        <Card cardInstance={cardInstance} area='stack' />
      </span>
    )
  }

  render() {
    return (
      <div id='stack'>
        {this.renderCards()}
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