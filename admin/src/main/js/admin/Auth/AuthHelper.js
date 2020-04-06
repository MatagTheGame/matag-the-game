export default class AuthHelper {
  static getToken() {
    return sessionStorage.getItem('token')
  }

  static setToken(token) {
    return sessionStorage.setItem('token', token)
  }

  static removeToken() {
    sessionStorage.removeItem('token')
  }

  static hasToken() {
    return sessionStorage.key('token')
  }
}