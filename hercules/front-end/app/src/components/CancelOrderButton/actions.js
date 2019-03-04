import httpClient from 'utils/http/index'
import { getAirCargoOrder } from 'containers/ACG/OrderManagementContainer/actions'
import { getOrderDetail } from 'containers/MWP/WisePortOrderContainer/actions'
import { getOrder } from 'containers/WMS/PriceVerificationContainer/actions'

const handler = {
  acg: (id, dispatch) => dispatch(getAirCargoOrder(id)),
  mwp: (id, dispatch) => dispatch(getOrderDetail(id)),
  wms: (id, dispatch) => dispatch(getOrder(id))
}

export const cancelOrder = ({id, orderType = ''}) => dispatch => {
  return httpClient.post(`/${orderType}/orders/${id}/cancellation`, { cancelReason: 'ManualCancellation' })
    .then(() => {
      handler[orderType](id, dispatch)
    })
    .catch(error => { throw error })
}