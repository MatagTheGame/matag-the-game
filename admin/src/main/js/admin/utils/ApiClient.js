import AuthHelper from 'admin/Auth/AuthHelper'

export default class ApiClient {
  static get(url) {
    return ApiClient.execute(url)
  }

  static getNoJson(url) {
    return ApiClient.executeNoJsonResponse(url)
  }

  static post(url, request) {
    return ApiClient.execute(url, request, 'POST')
  }

  static postNoJson(url, request) {
    return ApiClient.executeNoJsonResponse(url, request, 'POST')
  }

  static execute(url, request, method = 'GET') {
    return ApiClient.executeNoJsonResponse(url, request, method)
      .then((response) => response.json())
  }

  static executeNoJsonResponse(url, request, method = 'GET') {
    return fetch(url, {
      method: method,
      body: JSON.stringify(request),
      headers: {
        'Content-Type': 'application/json',
        'session': AuthHelper.getToken()
      }
    })
  }
}