import SockJs from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'

const socket = new SockJs('/game-ws')
const stompClient = Stomp.over(socket)

stompClient.sendEvent = (destination, body) => {
  stompClient.send('/api/game/' + destination, {}, JSON.stringify(body))
}

stompClient.init = (receiveCallback) => {
  stompClient.connect({}, () => {
    const sessionId = socket._transport.url.split('/')[5]

    stompClient.subscribe('/topic/events', (event) => {
      const eventBody = JSON.parse(event.body)
      receiveCallback(eventBody)
    })

    stompClient.subscribe('/user/' + sessionId + '/events', (event) => {
      const eventBody = JSON.parse(event.body)
      receiveCallback(eventBody)
    })

    stompClient.sendEvent('init', {})
  })
}

export default stompClient
