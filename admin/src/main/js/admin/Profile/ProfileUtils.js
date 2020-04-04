import ApiClient from '../utils/ApiClient'

export default class ProfileUtils {
  static getProfile() {
    return ApiClient.get('/profile')
  }
}