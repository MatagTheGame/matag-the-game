import TurnUtils from 'game/Turn/TurnUtils'

export default class UserInputUtils {
  static getDefaultChoiceForScry(state) {
    const inputRequiredActionParameter = TurnUtils.getInputRequiredActionParameter(state)
    let choice = '';
    for (let i = 1; i <= inputRequiredActionParameter; i++) {
      if (i > 1) {
        choice += ',';
      }
      choice += i;
    }
  }
}