import React, {Component} from 'react'
import Login from './Login/Login'
import Stats from './Stats/Stats'
import Intro from './Intro/Intro'

export default class Home extends Component {
  render() {
    return (
      <div>
        <Intro/>
        <Stats/>
        <Login/>
      </div>
    )
  }
}
