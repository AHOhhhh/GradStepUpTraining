import {set, map, filter} from 'lodash'
import {combineReducers} from 'redux';
import {mapProductInfo} from './mappers'
import * as types from './constants'

export const initialState = {
  list: [],
  productNameOptions: [],
  productPriceUnitOptions: []
}

const setProductIndex = (list) => map(list, (product, index) => ({
  ...product,
  index,
}))

const list = (state = [], action) => {
  switch (action.type) {
    case types.ADD_PRODUCT:
      return setProductIndex([...state, action.product])
    case types.ADD_PRODUCTS:
      const {productList, currency} = action
      return setProductIndex(map(productList, item => mapProductInfo(item, currency)))
    case types.EDIT_PRODUCT:
      const {product, index} = action
      return set(state, index, {...product, index})
    case types.DELETE_PRODUCT:
      return setProductIndex(filter(state, (product) => (product.index !== action.index)))
    case types.CLEAR_ACG_ORDER_CREATED_INFO:
      return []
    case types.CLEAR_PRODUCT_LIST:
      return []
    default:
      return state
  }
}

const productNameOptions = (state = [], action) => {
  switch (action.type) {
    case types.GET_PRODUCT_NAME_OPTIONS_SUCCESS:
      return action.req.data
    default:
      return state
  }
}

const productPriceUnitOptions = (state = [], action) => {
  switch (action.type) {
    case types.GET_PRODUCT_PRICE_UNIT_OPTIONS_SUCCESS:
      return action.req.data
    default:
      return state
  }
}

export default combineReducers({
  list,
  productNameOptions,
  productPriceUnitOptions,
})
