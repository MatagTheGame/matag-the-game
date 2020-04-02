import React from 'react'

export default function Phase(props) {
  let classes = props.status
  if (props.active) {
    classes += ' active'

    if (props.activeForPlayer) {
      classes += ' active-for-player'
    } else {
      classes += ' active-for-opponent'
    }
  }

  return <span key={props.name} className={classes}>{props.name}</span>
}