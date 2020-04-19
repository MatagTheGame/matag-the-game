export default class DateUtils {
  static parse(string) {
    return new Date(string)
  }

  static formatDateTime(date) {
    return date.toLocaleString()
  }
}