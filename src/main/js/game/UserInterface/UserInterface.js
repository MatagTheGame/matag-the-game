import React, {Component} from 'react'
import Connected from './Connected'
import HelpPage from './HelpPage'
import Message from './Message'
import StatusMessage from './StatusMessage'
import UserAction from './UserAction'
import AllUserInputs from './UserInputs/AllUserInputs'

export class UserInterface extends Component {
  render() {
    return (
      <>
        <UserAction/>
        <AllUserInputs/>
        <StatusMessage/>
        <Message/>
        <HelpPage/>
        <Connected/>
      </>
    )
  }
}