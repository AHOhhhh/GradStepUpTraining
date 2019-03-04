const initState = {
  departure: {},
  arrival: {},
  estimatedDeliveryTime: null,
  acgPrimaryNum: '',
  agentCode: ''
}

const shippingInfoReducer = (state = initState, action) => {
  switch (action.type) {
    case 'SELECT_SHIPPING_AIRPORT':
      return {
        ...state,
        changed: true,
        [action.port]: {
          ...state[action.port],
          isPickUpOrDropOff: false,
          addressId: null,
          airport: action.airport,
        }
      }
    case 'SELECT_PICK_OR_DELIVERY':
      return {
        ...state,
        [action.port]: {
          ...state[action.port],
          addressId: null,
          isPickUpOrDropOff: !state[action.port].isPickUpOrDropOff,
        }
      }
    case 'CHANGE_AIRPORT_LOCATION':
      return {
        ...state,
        [action.port]: {
          ...state[action.port],
          location: action.location,
        }
      }
    case 'ON_SELECT_SHIPPING_ADDRESS':
      return {
        ...state,
        [action.port]: {
          ...state[action.port],
          addressId: action.id,
        }
      }
    case 'CHANGE_ESTIMATED_DELIVERY_TIME':
      return {
        ...state,
        estimatedDeliveryTime: action.time
      }
    case 'CHANGE_PRIMARY_NUM':
      return {
        ...state,
        acgPrimaryNum: action.primaryNum
      }
    case 'CHANGE_AGENT_CODE':
      return {
        ...state,
        agentCode: action.agentCode
      }
    case 'CLEAR_ACG_ORDER_CREATED_INFO':
      return initState
    default:
      return state
  }
}

export default shippingInfoReducer;
