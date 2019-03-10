import React, {Fragment, PureComponent} from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'

class MaximizedCard extends PureComponent {
  constructor(props) {
    super(props);
    this.onWheel= this.onWheel.bind(this);
  }

  onWheel(e) {
    if (e.deltaY > 0) {
      this.props.minimizeCard()
    }
  }

  render() {
    if (this.props.maximizedCard) {
      return (
        <Fragment>
          <div id='modal-container' />
          <div id='maximized-card'
                  style={{backgroundImage: this.props.maximizedCard}}
                  onWheel={this.onWheel}/>
        </Fragment>
      )
    } else {
      return null
    }
  }
}

const minimizeCardEvent = () => {
  return {
    type: 'MAXIMIZE_MINIMIZE_CARD',
    value: {
      cardImage: undefined
    }
  }
}

const mapStateToProps = state => {
  return {
    maximizedCard: state.maximizedCard
  }
}

const mapDispatchToProps = dispatch => {
  return {
    minimizeCard: bindActionCreators(minimizeCardEvent, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(MaximizedCard)
