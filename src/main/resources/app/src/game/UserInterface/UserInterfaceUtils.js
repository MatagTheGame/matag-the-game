import {set} from 'lodash'

export default class UserInterfaceUtils {
  static setMessage(state, message, closable = true) {
    set(state, 'userInterface.message', {text: message, closable: closable})
  }

  static unsetMessage(state) {
    UserInterfaceUtils.setMessage(state, undefined)
  }
}