import SockJs from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'

const socket = new SockJs('/matag/game/game-ws')
const stompClient = Stomp.over(socket)
const gameId = window.location.pathname.split('/').pop()

stompClient.sendEvent = (destination, body) => {
  const headers = {
    token: sessionStorage.getItem('token'),
    gameId: gameId
  }
  stompClient.send(`/api/game/${destination}`, headers, JSON.stringify(body))
}

stompClient.sendHeartbeat = () => {
  stompClient.send('/api/healthcheck')
}

stompClient.init = (receiveCallback) => {
  stompClient.connect({}, () => {
    const urlParts = socket._transport.url.split('/')
    const sessionId = urlParts[urlParts.length - 2]

    stompClient.subscribe('/topic/events', (event) => {
      const eventBody = JSON.parse(event.body)
      receiveCallback(eventBody)
    })

    stompClient.subscribe(`/user/${sessionId}/events`, (event) => {
      console.log('received event', event)
      const eventBody = JSON.parse(event.body)
      receiveCallback(eventBody)
    })

    stompClient.sendEvent('init', {})
  })
}

export default stompClient
