import {combineReducers} from 'redux'
import offerSelectionReducer from './offerSelectionReducer'
import paymentReducer from './paymentReducer'
import orderReducer from './orderReducer'
import changeRecordsReducer from './changeRecordsReducer'
import inServiceReducer from '../InServiceStep/reducer'

export default combineReducers({
  order: orderReducer,
  offerSelection: offerSelectionReducer,
  payment: paymentReducer,
  inService: inServiceReducer,
  changeRecordsReducer
})
