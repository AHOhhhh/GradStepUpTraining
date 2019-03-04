import {combineReducers} from 'redux'

import orderReducer from './SCFOrderContainer/reducer'
import orderQualificationReducer from './CreateSCFOrderContainer/reducer'

export default combineReducers({
  order: orderReducer,
  orderQualification: orderQualificationReducer
})
