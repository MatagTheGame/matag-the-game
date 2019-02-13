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
    client: stompClient
  }
}

export default (state, action) => {
  switch (action.type) {
    default:
      return initSocketAndClient()
  }
}