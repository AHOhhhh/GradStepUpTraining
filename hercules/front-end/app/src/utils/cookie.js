import cookie from 'cookie-machine'

const host = window.location.host
const config = {path: '/'}

// todo: need to change
if (/\.hlp\.fun/.test(host)) {
  config.domain = '.hlp.fun'
} else if (/haipingx\.com/.test(host)) {
  config.domain = '.haipingx.com'
}


export default {
  init(res) {
    return cookie.init(res)
  },
  set(key, value) {
    const options = {...config}
    return cookie.set(key, value, options)
  },
  get(key) {
    return cookie.get(key)
  },
  remove(key) {
    const options = {...config}
    return cookie.remove(key, options)
  }
}