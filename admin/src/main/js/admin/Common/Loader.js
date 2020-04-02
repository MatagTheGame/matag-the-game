import React, {Component} from 'react'
import './loader.scss'

export default class Loader extends Component {
  render() {
    return (
      <div>
        <img className='loader' alt='loader' src='/img/loader.gif' />
      </div>
    )
  }
}
