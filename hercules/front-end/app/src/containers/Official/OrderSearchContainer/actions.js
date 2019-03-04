import httpClient from '../../../utils/http/index'
import * as types from '../../ACG/share/constants/index'

export const getOrders = (orderIds, captchaId, captcha) => ({
  type: types.GET_ACG_ORDERS,
  promise: httpClient.get(`/acg/orders?ids=${orderIds}&captchaId=${captchaId}&captcha=${captcha}`)
})
