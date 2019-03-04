import moment from 'moment'
import {compact, isNumber, trim, lt} from 'lodash'

const dateFormat = 'YYYY-MM-DDTHH:mm:ssZ'

function getCurrencySymbol(currency) {
  switch (currency) {
    case 'CNY':
      return '¥'
    case 'USD':
      return '$'
    default:
      return '¥'
  }
}

export function formatPrice(price, currency) {
  const currencySymbol = getCurrencySymbol(currency);
  if (lt(price, 1)) {
    return `${currencySymbol} ${price}`
  }

  const originNumber = (price || 0).toString()
  const numbers = originNumber.split('');
  const result = [];
  const isContainsDecimal = originNumber.includes('.')
  const formatString = isContainsDecimal ? originNumber.split('.')[0].split('') : numbers
  formatString.reverse().forEach((num, index) => {
    result.push(num);
    if ((index + 1) % 3 === 0 && index < numbers.length - 1) {
      result.push(',')
    }
  })
  const formattedPrice = isContainsDecimal ? result.reverse().join('').concat('.', originNumber.split('.')[1]) : result.reverse().join('')
  return `${currencySymbol} ${trim(formattedPrice, ',')}`
}

export const getUTCTime = (time) => {
  const localeTimeString = moment(time).set({hour: 0, minute: 0, second: 0})
  return moment.utc(localeTimeString).format(dateFormat)
}

export const formatDate = (timestamp) => {
  return moment(timestamp).format('YYYY-MM-DD')
}

export const formatDateWithHourMinute = (timestamp) => {
  const thisMoment = isNumber(timestamp) ? moment.unix(timestamp) : moment(timestamp)
  return thisMoment.format('YYYY-MM-DD HH:mm')
}

export const formatDateTime = (timestamp) => {
  const thisMoment = isNumber(timestamp) ? moment.unix(timestamp) : moment(timestamp)
  return thisMoment.format('YYYY-MM-DD HH:mm:ss')
}

export function concatArgs(separator, ...args) {
  return compact(args).join(separator)
}

export const formatRateRange = (record) => {
  const rateFloor = record && record.rateFloor ? `${record.rateFloor}%` : null
  const rateCap = record && record.rateCap ? `${record.rateCap}%` : null
  return rateFloor && rateCap
    ? `${rateFloor} - ${rateCap}`
    : (rateFloor || rateCap || '-')
}

export const convertCharge = (charge, currency) => charge.toLocaleString('zh-hans', {style: 'currency', currency, minimumFractionDigits: 2})