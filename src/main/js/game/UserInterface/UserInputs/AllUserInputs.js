import React, {Component} from 'react'
import PlayableAbilities from './PlayableAbilities'
import ScryUserInput from './ScryUserInput'

export default class AllUserInputs extends Component {
  render() {
    return (
      <>
        <PlayableAbilities/>
        <ScryUserInput/>
      </>
    )
  }
}