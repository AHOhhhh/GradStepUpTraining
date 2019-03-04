import {combineReducers} from 'redux'
import {routerReducer} from 'react-router-redux'
import {pick} from 'lodash'
import auth from './containers/User/LoginContainer/reducer'
import enterpriseInfo from './containers/User/EnterpriseInfoContainer/reducer'
import wms from './containers/WMS/share/reducer'
import contact from './containers/shared/Contact/reducer'

import enterpriseUser from './containers/User/LandingPageContainer/EnterpriseUserList/reducer'
import admin from './containers/Admin/share/reducer'

import wisePortCreateOrder from './containers/MWP/CreateOrderContainer/reducer'
import wisePortOrder from './containers/MWP/WisePortOrderContainer/reducer'
import scf from './containers/SCF/reducers'

import acg from './containers/ACG/share/reducer'
import productList from './components/ProductList/reducer'
// import platformAdmin from './containers/Admin/EnterpriseListContainer/Authorized/ViewEnterpriseInfoContainer/AuthorizationHistory/reducer'
import platformAdmin from './containers/Admin/share/reducer/platformAdminReducer'
import orderBill from './containers/Admin/share/reducer/orderBillReducer'
import checkoutCounter from './containers/CheckoutCounter/CheckoutCounterContainer/reducer'
import auditPayment from './containers/Admin/share/reducer/auditPayment'
import fundsInfo from './containers/Admin/FundsListContainer/reducer'
import { LOGOUT_USER } from './containers/User/LoginContainer/constants';

const appReducer = combineReducers({
  routing: routerReducer,
  auth,
  enterpriseInfo,
  enterpriseUser,
  wms,
  admin,
  contact,
  wisePortCreateOrder,
  wisePortOrder,
  acg,
  platformAdmin,
  scf,
  checkoutCounter,
  auditPayment,
  productList,
  fundsInfo,
  orderBill
})

const rootReducer = (state, action) => {
  const rootState = action.type === LOGOUT_USER ? pick(state, 'routing') : state
  return appReducer(rootState, action)
}

export default rootReducer
