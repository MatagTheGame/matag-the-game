import SockJs from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'

const socket = new SockJs('/game-ws')
const stompClient = Stomp.over(socket)
const gameId = window.location.pathname.replace('/ui/game/', '')

stompClient.sendEvent = (destination, body) => {
  const headers = {
    token: sessionStorage.getItem('token'),
    gameId: gameId
  }
  stompClient.send(`/api/game/${destination}`, headers, JSON.stringify(body))
}

stompClient.init = (receiveCallback) => {
  stompClient.connect({}, () => {
    const sessionId = socket._transport.url.split('/')[5]

    stompClient.subscribe('/topic/events', (event) => {
      const eventBody = JSON.parse(event.body)
      receiveCallback(eventBody)
    })

    stompClient.subscribe(`/user/${sessionId}/events`, (event) => {
      const eventBody = JSON.parse(event.body)
      receiveCallback(eventBody)
    })

    stompClient.sendEvent('init', {})
  })
}

export default stompClient
