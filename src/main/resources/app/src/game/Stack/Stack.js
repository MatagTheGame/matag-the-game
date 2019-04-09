import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import Card from '../Card/Card'

class Stack extends PureComponent {

  renderCards(cards) {
    return cards.map((cardInstance) =>
      <span key={cardInstance.id}>
        <Card cardInstance={cardInstance} />
      </span>
    )
  }

  render() {
    return (
      <div id='stack'>
        {this.renderCards(this.props.stack)}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    stack: get(state, 'stack', [])
  }
}

export default connect(mapStateToProps)(Stack)