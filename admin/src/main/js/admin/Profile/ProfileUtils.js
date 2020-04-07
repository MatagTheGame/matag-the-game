import ApiClient from 'admin/utils/ApiClient'

export default class ProfileUtils {
  static getProfile() {
    return ApiClient.get('/profile')
  }
}