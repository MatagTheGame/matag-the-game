export default class ApiClient {
  static get(url) {
    return ApiClient.execute(url)
  }

  static post(url, request) {
    return ApiClient.execute(url, request, 'POST')
  }

  static postNoRedirect(url, request) {
    return ApiClient.executeNoJsonResponse(url, request, 'POST')
  }

  static execute(url, request, method = 'GET') {
    return ApiClient.executeNoJsonResponse(url, request, method)
      .then((response) => response.json())
  }

  static executeNoJsonResponse(url, request, method = 'GET') {
    return fetch(url, {method: method, body: JSON.stringify(request), headers: {'Content-Type': 'application/json'}})
  }
}