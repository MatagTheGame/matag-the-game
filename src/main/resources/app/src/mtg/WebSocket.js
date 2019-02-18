import SockJs from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'

const socket = new SockJs('/mtg-ws')
const stompClient = Stomp.over(socket)

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

    stompClient.send('/api/init', {}, '{}')
  })
}

export default stompClient
