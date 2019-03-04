import {combineReducers} from 'redux';
import * as types from './constants';

const init = [];

const enterprisePayMethodsReducer = (state = init, action) => {
  switch (action.type) {
    case types.GET_ENTERPRISE_PAY_METHODS_SUCCESS:
      return action.req.data;
    default:
      return state
  }
};

export default combineReducers({
  enterprisePayMethods: enterprisePayMethodsReducer
})