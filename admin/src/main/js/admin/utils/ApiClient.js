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

  static postToUrl(path, params) {
    const form = document.createElement("form");
    form.setAttribute("method", 'POST');
    form.setAttribute("action", path);

    for(const key in params) {
      if(params.hasOwnProperty(key)) {
        const hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", key);
        hiddenField.setAttribute("value", params[key]);

        form.appendChild(hiddenField);
      }
    }

    document.body.appendChild(form);
    form.submit();
  }
}