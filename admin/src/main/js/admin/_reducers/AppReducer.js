import AuthHelper from '../Auth/AuthHelper'

const clone = (object) => {
  if (!object) {
    object = {}
  }
  return JSON.parse(JSON.stringify(object))
}

export default (state, action) => {
  const newState = clone(state)

  if (!state) {
    return {session: {loading: true, token: AuthHelper.getToken()}}

  } else if (action.type === 'PROFILE_LOADED') {
    newState.session.loading = false
    newState.session.profile = action.value

  } else if (action.type === 'LOAD_STATS') {
    newState.stats = {loading: true}

  } else if (action.type === 'STATS_LOADED') {
    newState.stats.loading = false
    newState.stats.value = action.value

  } else if (action.type === 'LOGIN_LOADING') {
    newState.login = {loading: true}

  } else if (action.type === 'LOGIN_RESPONSE') {
    newState.login = {loading: false}
    if (action.value.error) {
      newState.login.error = action.value.error
    } else {
      AuthHelper.setToken(action.value.token)
      newState.session = {
        loading: false,
        token: action.value.token,
        profile: action.value.profile
      }
    }

  } else {
    throw new Error(`Unknown action type ${action.type}`)
  }

  return newState
}