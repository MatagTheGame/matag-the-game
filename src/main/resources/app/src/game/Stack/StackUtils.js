export default class StackrUtils {
  static isStackEmpty(state) {
    return !state.stack || state.stack.length === 0
  }
}