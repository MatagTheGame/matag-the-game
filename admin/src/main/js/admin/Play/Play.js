import React, {Component} from 'react'
import {connect} from 'react-redux'
import './play.scss'

class Play extends Component {
  constructor(props) {
    super(props)
    this.handlePlay = this.handlePlay.bind(this)
    this.toggle = this.toggle.bind(this)
    this.state = {colors: []}
  }

  handlePlay(event) {
    event.preventDefault()

    if (this.state.colors.length === 0) {
      alert('You need to select at least one color.')

    } else {
      alert('Play random game of colors: ' + this.state.colors)
    }
  }

  isSelected(color) {
    return this.state.colors.indexOf(color) > -1
  }

  toggle(color) {
    const colors = this.state.colors
    if (this.isSelected(color)) {
      colors.splice(colors.indexOf('b'), 1)
      this.setState({colors: colors})

    } else {
      this.setState({colors: [...colors, color]})
    }
  }

  render() {
    return (
      <section id='play'>
        <h2>Play</h2>
        <form className='matag-form' onSubmit={this.handlePlay}>
          <p>Choose which colors you want to play:</p>
          <ul>
            <li>
              <input type='checkbox' id='color-white' name='white' checked={this.isSelected('white')} onChange={() => this.toggle('white')}/>
              <label htmlFor='color-white'><img src='/img/symbols/WHITE.png' alt='white'/>White</label>
            </li>
            <li>
              <input type='checkbox' id='color-blue' name='blue' checked={this.isSelected('blue')} onChange={() => this.toggle('blue')}/>
              <label htmlFor='color-blue'><img src='/img/symbols/BLUE.png' alt='blue'/>Blue</label>
            </li>
            <li>
              <input type='checkbox' id='color-black' name='black' checked={this.isSelected('black')} onChange={() => this.toggle('black')}/>
              <label htmlFor='color-black'><img src='/img/symbols/BLACK.png' alt='black'/>Black</label>
            </li>
            <li>
              <input type='checkbox' id='color-red' name='red' checked={this.isSelected('red')} onChange={() => this.toggle('red')}/>
              <label htmlFor='color-red'><img src='/img/symbols/RED.png' alt='red'/>Red</label>
            </li>
            <li>
              <input type='checkbox' id='color-green' name='green' checked={this.isSelected('green')} onChange={() => this.toggle('green')}/>
              <label htmlFor='color-green'><img src='/img/symbols/GREEN.png' alt='green'/>Green</label>
            </li>
          </ul>
          <div className='grid three-columns'>
            <div/>
            <input type='submit' id='play-button' value='Play' onClick={this.handlePlay}/>
          </div>
        </form>
      </section>
    )
  }
}

const mapStateToProps = state => {
  return {}
}

const mapDispatchToProps = dispatch => {
  return {}
}

export default connect(mapStateToProps, mapDispatchToProps)(Play)