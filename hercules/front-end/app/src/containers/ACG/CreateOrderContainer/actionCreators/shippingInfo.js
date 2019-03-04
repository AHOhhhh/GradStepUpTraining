export const onAirportSelect = (airport, port) => (dispatch) => (
  dispatch({
    type: 'SELECT_SHIPPING_AIRPORT',
    airport,
    port,
  })
)

export const onPickOrDeliverySelect = (port) => (dispatch) => (
  dispatch({
    type: 'SELECT_PICK_OR_DELIVERY',
    port,
  })
)

export const changeAirportLocation = (port, location) => (dispatch) => (
  dispatch({
    type: 'CHANGE_AIRPORT_LOCATION',
    port,
    location,
  })
)

export const onSelectShippingAddressId = (port, id) => (dispatch) => {
  dispatch({
    type: 'ON_SELECT_SHIPPING_ADDRESS',
    id,
    port,
  })
}

export const changeEstimatedDeliveryTime = (time) => (dispatch) => (
  dispatch({
    type: 'CHANGE_ESTIMATED_DELIVERY_TIME',
    time,
  })
)

export const changeAcgPrimaryNum = (primaryNum) => (dispatch) => (
  dispatch({
    type: 'CHANGE_PRIMARY_NUM',
    primaryNum,
  })
)

export const changeAgentCode = (agentCode) => (dispatch) => (
  dispatch({
    type: 'CHANGE_AGENT_CODE',
    agentCode,
  })
)
