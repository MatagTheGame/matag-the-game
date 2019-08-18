import React, {Component, Fragment} from 'react'
import UserAction from 'Main/game/UserInterface/UserAction'
import PlayableAbilities from 'Main/game/UserInterface/PlayableAbilities'
import StatusMessage from 'Main/game/UserInterface/StatusMessage'
import Message from 'Main/game/UserInterface/Message'

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
      </Fragment>
    )
  }
}