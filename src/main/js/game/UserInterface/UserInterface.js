import React, {Component, Fragment} from 'react'
import Connected from './Connected'
import HelpPage from './HelpPage'
import Message from './Message'
import PlayableAbilities from './PlayableAbilities'
import StatusMessage from './StatusMessage'
import UserAction from './UserAction'

export class UserInterface extends Component {
  constructor(props) {
    super(props)
  }

  render() {
    return (
      <Fragment>
        <UserAction/>
        <PlayableAbilities/>
        <StatusMessage/>
        <Message/>
        <HelpPage/>
        <Connected/>
      </Fragment>
    )
  }
}