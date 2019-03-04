import {userIsAuthenticated, userIsNotAuthenticated} from 'utils'
import {USER_ROLE} from 'constants'
import {notification} from 'antd'
import {getStoreInstance} from '../../store'

const ENTERPRISE_USER = [USER_ROLE.enterpriseUser, USER_ROLE.enterpriseAdmin]

const errorLoading = err =>
  console.error('Dynamic loading failed' + err) // eslint-disable-line

const loadRoute = cb =>
  module =>
    cb(null, userIsAuthenticated(module.default))

const isPlatformAdmin = (module) => {
  const store = getStoreInstance()
  const {user} = store.getState().auth
  if (user && user.role !== 'PlatformAdmin') {
    notification.open({message: '您没有权限访问'})
  }
  return userIsAuthenticated(module)
}

function checkInvalidRoleAndRedirect(nextState, replace, callback) { // eslint-disable-line
  const store = getStoreInstance()
  const user = store.getState().auth.user
  const path = store.getState().routing.locationBeforeTransitions.pathname
  if ((path.indexOf('/admin/') > -1)
    && user && ENTERPRISE_USER.includes(user.role)) {
    replace('/')
    callback()
  }
  callback()
}

const routes = [
  {
    path: 'enterprise_list',
    getComponent(location, callback) {
      import('./EnterpriseListContainer')
        .then((module) => callback(null, isPlatformAdmin(module.default)))
        .catch(err => errorLoading(err))
    }
  },
  {
    path: 'orders',
    getComponent(location, callback) {
      import('./OrderList')
        .then(loadRoute(callback))
        .catch(err => errorLoading(err))
    },
    onEnter: checkInvalidRoleAndRedirect
  },
  {
    path: 'login',
    getComponent(location, callback) {
      import('./LoginContainer')
        .then(module => callback(null, userIsNotAuthenticated(module.default)))
        .catch(err => errorLoading(err))
    },
  },
  {
    path: 'funds',
    getComponent(location, callback) {
      import('./FundsListContainer')
        .then(loadRoute(callback))
        .catch(err => errorLoading(err))
    },
  },
  {
    path: 'enterprises/:id',
    getComponent(location, callback) {
      import('./EnterpriseDetailContainer')
        .then(loadRoute(callback))
        .catch(err => errorLoading(err))
    }
  },
  {
    path: 'view_enterprise_info/:id',
    getComponent(location, callback) {
      import('./EnterpriseListContainer/Authorized/ViewEnterpriseInfoContainer')
        .then(loadRoute(callback))
        .catch(err => errorLoading(err))
    },
  },
  {
    path: ':orderType/orders/:orderId',
    getComponent(location, callback) {
      import('./OrderDetailPreviewContainer')
        .then(loadRoute(callback))
        .catch(err => errorLoading(err))
    },
  },
  {
    path: 'preview_enterprise_info/:id',
    getComponent(location, callback) {
      import('./PreviewEnterpriseInfo')
        .then(loadRoute(callback))
        .catch(err => errorLoading(err))
    },
  },
  {
    path: 'operation_records',
    getComponent(location, callback) {
      import('./OperationRecordsContainer')
        .then(loadRoute(callback))
        .catch(err => errorLoading(err))
    }
  },
  {
    path: 'order_bills',
    getComponent(location, callback) {
      import('./OperationOrderBillsContainer')
        .then(loadRoute(callback))
        .catch(err => errorLoading(err))
    }
  }
]

export default routes
