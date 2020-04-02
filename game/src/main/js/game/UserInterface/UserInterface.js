import React, {Component, Fragment} from 'react'
import UserAction from './UserAction'
import PlayableAbilities from './PlayableAbilities'
import StatusMessage from './StatusMessage'
import Message from './Message'
import HelpPage from './HelpPage'

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
      </Fragment>
    )
  }
}