import React, {Component} from 'react'

export default class Intro extends Component {
  render() {
    return (
      <section id='intro'>
        <p>
          Welcome to Matag, a web based version of <strong>Magic: The Gathering</strong> where you can play using just your browser.
        </p>
        <p>
          The game is open source and you can find the code and contribute or contact the creators at <a href='https://github.com/antonioalonzi/matag' target='_blank'>matag</a> github.<br/>
          (Please note that we are not affiliated in any way with the MTG creators, nor we claim any copyright over their game or art assets.)
        </p>
        <p>
          In case you are the only one online, you can play against yourself by opening two browser sessions (windows/tabs) at this address.
        </p>
      </section>
    )
  }
}
