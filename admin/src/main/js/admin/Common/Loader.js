import React, {Component} from 'react'
import './loader.scss'

export default class Loader extends Component {
  constructor(props) {
    super(props)
  }

  getClassNames() {
    if (this.props.center) {
      return 'center'
    }

    if (this.props.fullscreen) {
      return 'fullscreen-loader'
    }
  }

  render() {
    return (
      <div className={this.getClassNames()}>
        <img className='loader' alt='loader' src='/img/loader.gif' />
      </div>
    )
  }
}
