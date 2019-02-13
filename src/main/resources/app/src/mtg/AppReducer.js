import SockJs from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'

const initSocketAndClient = () => {
  let socket = new SockJs('/mtg-ws')
  let stompClient = Stomp.over(socket)

  stompClient.connect({}, () => {
    const sessionId = socket._transport.url.split('/')[5]

    stompClient.subscribe('/topic/events', (event) => {
      const eventBody = JSON.parse(event.body)
      console.log(eventBody)
    })

    stompClient.subscribe('/user/' + sessionId + '/events', (event) => {
      const eventBody = JSON.parse(event.body)
      console.log(eventBody)
    })

    stompClient.send('/api/init', {}, '{}')
  })

  return {
    socket: socket,
    client: stompClient,
    player: {
      hand: ['forest', 'mountain', 'alpha_tyrranax'],
      deck: ['card'],
      battlefield: ['forest', 'forest', 'mountain', 'mountain', 'mountain']
    },
    opponent: {
      hand: ['card', 'card', 'card', 'card'],
      deck: ['card'],
      battlefield: ['forest', 'forest', 'mountain']
    }
  }
}

export default (state, action) => {
  switch (action.type) {
    default:
      return initSocketAndClient()
  }
}