import { combineReducers } from 'redux'
import * as types from '../constants';
import shippingInfo from '../../CreateOrderContainer/reducers/shippingInfo'
import errors from '../../CreateOrderContainer/reducers/errors'

export const initialState = {
  airports: [],
  order: {},
  orderPrice: {}
};

const getAirportsReducer = (state = [], action) => {
  switch (action.type) {
    case types.GET_AIRPORTS_INFO_SUCCESS:
      return action.req.data
    case types.GET_AIRPORTS_INFO_FAILURE:
      return []
    case types.GET_AIRPORTS_INFO_REQUEST:
      return []
    default:
      return state
  }
}

const getACGOrderReducer = (state = {}, action) => {
  switch (action.type) {
    case types.GET_ACG_ORDER_REQUEST:
      return {
        ...state,
        success: null,
        failed: null,
      }
    case types.GET_ACG_ORDER_SUCCESS:
      return {
        ...action.req.data,
        success: true,
        failed: false
      }
    case types.GET_ACG_ORDER_FAILURE:
      return {
        ...state,
        success: false,
        failed: true,
        error: action.error
      }
    default:
      return state
  }
}

const getACGOrdersReducer = (state = {}, action) => {
  switch (action.type) {
    case types.GET_ACG_ORDERS_REQUEST:
      return {
        ...state,
        ...action.status
      }
    case types.GET_ACG_ORDERS_SUCCESS:
      return {
        data: action.req.data,
        ...action.status
      }
    case types.GET_ACG_ORDERS_FAILURE:
      return {
        ...state,
        ...action.status,
        error: action.error
      }
    default:
      return state
  }
}

const getAirCargoOrderPriceReducer = (state = {}, action) => {
  switch (action.type) {
    case types.GET_AIRCARGO_ORDER_PRICE_REQUEST:
      return {
        error: {},
        status: 'loading',
      }
    case types.GET_AIRCARGO_ORDER_PRICE_FAILURE:
      return {
        error: action.error,
        status: 'error',
      }
    case types.GET_AIRCARGO_ORDER_PRICE_SUCCESS:
      return {
        ...state,
        ...action.req.data.content[0],
        status: 'success',
      }
    default:
      return state
  }
};

const validationStatusReducer = (state = {}, action) => {
  switch (action.type) {
    case types.CHANGE_VALIDATION_STATUS:
      return action.status
    default:
      return state
  }
}

export default combineReducers({
  errors,
  shippingInfo,
  airports: getAirportsReducer,
  order: getACGOrderReducer,
  orders: getACGOrdersReducer,
  orderPrice: getAirCargoOrderPriceReducer,
  isValidating: validationStatusReducer,
});
