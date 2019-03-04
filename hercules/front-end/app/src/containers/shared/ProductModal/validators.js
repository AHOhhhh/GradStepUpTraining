import { isNaN, isUndefined } from 'lodash'

export const validateNumLength = (len, precision) => {
  return {
    validator: (rule, value, callback) => {
      const stringify = (value && value.toString().split('.')) || []

      if (value && (isNaN(parseInt(value)) || value <= 0)) {
        callback('请输入大于0的数字')
      }

      if (stringify.length > 2) {
        callback('请输入正确的数字格式')
      }

      if (len && stringify[0] && stringify[0].length > len) {
        callback(`整数最多${len}位`)
      }

      if (precision && stringify[1] && stringify[1].length > precision) {
        callback(`小数最多${precision}位`)
      } else if (!precision && !isUndefined(stringify[1])) {
        callback('不支持小数')
      }
      callback()
    }
  }
}
