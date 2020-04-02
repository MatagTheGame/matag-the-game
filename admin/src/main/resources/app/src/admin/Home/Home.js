import React from 'react'
import Login from './Login/Login'
import Stats from './Stats/Stats'
import Intro from './Intro/Intro'

export default function Home() {
  return (
    <div>
      <Intro/>
      <Stats/>
      <Login/>
    </div>
  )
}
