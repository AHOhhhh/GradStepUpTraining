import _ from 'lodash'
import {orderCancelStatus} from '../constants'

const hasReferenceOrder = order => !_.isEmpty(_.get(order, 'referenceOrder'))

export const mapOrderListWithReferenceOrder = (orders) => {
  const allOrders = []
  _.forEach(orders, (item) => {
    if (hasReferenceOrder(item)) {
      allOrders.push({...item, isReferenced: true})
      allOrders.push(item.referenceOrder)
    } else {
      allOrders.push(item)
    }
  })
  return allOrders
}

const uniqueOrdersById = orders => _.uniqBy(orders, 'id')

const formatFunc = _.flow([mapOrderListWithReferenceOrder, uniqueOrdersById])

export const formatOrderList = (orders) => ({
  all: formatFunc(orders),
  submitted: formatFunc(orders),
  onGoing: formatFunc(orders),
  closed: formatFunc(orders),
  cancelled: formatFunc(orders)
})

export const canCancel = ({ orderType, status }) => {
  if (orderType === 'scf') {
    return false
  }
  return _.includes(orderCancelStatus[orderType], status)
}