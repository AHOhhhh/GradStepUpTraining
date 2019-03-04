import _ from 'lodash'

import {ACG_STEPS, ORDER_STATUS} from 'containers/ACG/OrderManagementContainer/constants/orderStatus'

export const getStepper = (orderStatus, cancelledReason) => {
  return orderStatus === ORDER_STATUS.cancelled ? _.find(ACG_STEPS, ({cancelReason}) => cancelReason === cancelledReason) : _.find(ACG_STEPS, ({status}) => _.includes(status, orderStatus))
}

export const getCurrentStepIndex = (orderStatus, cancelledReason) => {
  const step = getStepper(orderStatus, cancelledReason)
  return step ? step.id : null
}

export const orderIsSubmitted = ({status, cancelReason}) => {
  const currentStepIndex = getCurrentStepIndex(status, cancelReason)
  return currentStepIndex && currentStepIndex > 1
}
