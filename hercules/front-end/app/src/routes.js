import {
  enterpriseUserIsNotResetPassword,
  userIsAuthenticated,
  userIsNotEnterpriseAdmin,
  userIsNotAuthenticated,
  userIsNotPlatformAdminAdmin
} from 'utils'
import {USER_ROLE} from 'constants'
import {includes} from 'lodash'
import {notification} from 'antd'
import AppContainer from './containers/AppContainer'
import {getStoreInstance} from './store'

const errorLoading = _ => _ // console.error('Dynamic loading failed' + err)

const loadRoute = (cb, needAuth = true) =>
  module => {
    cb(null, needAuth ? userIsAuthenticated(module.default) : module.default)
  }

function isEnterpriseRegistered(nextState, replace, callback) {
  const store = getStoreInstance()
  if (store.getState().auth.enterpriseId && !nextState.location.state.isEdit) {
    replace('/')
    callback()
  }
  callback()
}

const isEnterpriseUser = (module) => {
  const store = getStoreInstance()
  const {user} = store.getState().auth
  if (user && user.role !== 'EnterpriseUser') {
    notification.open({message: '您没有权限访问'})
  }
  return userIsAuthenticated(userIsNotEnterpriseAdmin(userIsNotPlatformAdminAdmin(module)))
}

const checkIsNotResetPassword = (nextState, replace, callback) => {
  const store = getStoreInstance()
  const user = store.getState().auth.user
  if (!user.resettable) {
    replace('/change_password')
    callback()
  }
  callback()
}

const checkPasswordResettableAndRedirect = (nextState, replace, callback) => {
  const store = getStoreInstance()
  const user = store.getState().auth.user
  if (user.resettable) {
    replace('/')
    callback()
  }
  callback()
}

const checkRole = (nextState, replace, callback) => {
  const store = getStoreInstance()
  if (store.getState().auth.user && includes([USER_ROLE.platformAdmin, USER_ROLE.platformAdmin], store.getState().auth.user.role)) {
    replace('/admin/orders')
    callback()
  }
  callback()
}

export const routes = {
  component: AppContainer,
  path: '/',
  indexRoute: {
    getComponent(location, callback) {
      import('./containers/User/LandingPageContainer')
        .then(loadRoute(callback))
        .catch(err => errorLoading(err))
    },
    onEnter: checkRole
  },
  childRoutes: [
    {
      path: '/admin',
      getChildRoutes(partialNextState, callback) {
        import('./containers/Admin/routes')
          .then(loadRoute(callback, false))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/wms/create',
      getComponent(nextState, callback) {
        import('./containers/WMS/CreateOrderContainer')
          .then(module => callback(null, isEnterpriseUser(enterpriseUserIsNotResetPassword(module.default))))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/:orderType/orders/:orderId/payment_failure',
      getComponent(location, callback) {
        import('./containers/CheckoutCounter/PaymentFailureContainer') // eslint-disable-line block-scoped-var
          .then(module => callback(null, userIsAuthenticated(enterpriseUserIsNotResetPassword(module.default))))
          .catch(err => errorLoading(err))
      },
    },
    {
      path: '/wms/orders/:orderId',
      getComponent(nextState, callback) {
        import('./containers/WMS/OrderContainer')
          .then(module => callback(null, userIsAuthenticated(userIsNotPlatformAdminAdmin(enterpriseUserIsNotResetPassword(module.default)))))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/wms/payment/:orderId',
      getComponent(location, callback) {
        import('./containers/WMS/PaymentContainer')
          .then(module => callback(null, isEnterpriseUser(enterpriseUserIsNotResetPassword(module.default))))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/acg/create',
      getComponent(location, callback) {
        import('./containers/ACG/CreateOrderContainer')
          .then(module => callback(null, isEnterpriseUser(enterpriseUserIsNotResetPassword(module.default))))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/acg/orders/:orderId',
      getComponent(location, callback) {
        import('./containers/ACG/OrderManagementContainer')
          .then(module => callback(null, userIsAuthenticated(userIsNotPlatformAdminAdmin(enterpriseUserIsNotResetPassword(module.default)))))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/order_search',
      getComponent(location, callback) {
        import('./containers/Official/OrderSearchContainer')
          .then(loadRoute(callback, false))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/login',
      getComponent(location, callback) {
        import('./containers/User/LoginContainer')
          .then(module => callback(null, userIsNotAuthenticated(module.default)))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/logout',
      getComponent(location, callback) {
        import('./containers/User/LogoutContainer')
          .then(loadRoute(callback, false))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/oauth2/authorize',
      getComponent(location, callback) {
        import('./containers/SSO/AuthorizeContainer')
          .then(loadRoute(callback))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/terms',
      getComponent(location, callback) {
        import('./containers/User/SignUpTermsContainer')
          .then(loadRoute(callback, false))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/upload_material',
      getComponent(location, callback) {
        import('./containers/User/UploadMaterialContainer')
          .then(loadRoute(callback))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/signup',
      getComponent(location, callback) {
        import('./containers/User/SignUpContainer')
          .then(loadRoute(callback, false))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/signup_enterprise',
      getComponent(location, callback) {
        import('./containers/User/SignUpEnterpriseContainer')
          .then(loadRoute(callback))
          .catch(err => errorLoading(err))
      },
      onEnter: isEnterpriseRegistered
    },
    {
      path: '/signup_succeed',
      getComponent(location, callback) {
        import('./containers/User/SignupSucceedContainer')
          .then(loadRoute(callback))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/enterprise_info',
      getComponent(location, callback) {
        import('./containers/User/EnterpriseInfoContainer')
          .then((module) => callback(null, userIsAuthenticated(enterpriseUserIsNotResetPassword(module.default))))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/contact',
      getComponent(location, callback) {
        import('./containers/User/ContactInfoContainer')
          .then((module) => callback(null, isEnterpriseUser(enterpriseUserIsNotResetPassword(module.default))))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/orders',
      getComponent(location, callback) {
        import('./containers/User/OrderListContainer')
          .then((module) => callback(null, userIsAuthenticated(enterpriseUserIsNotResetPassword(module.default))))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '/change_password',
      onEnter: checkPasswordResettableAndRedirect,
      getComponent(location, callback) {
        import('./containers/User/ChangePasswordContainer')
          .then(loadRoute(callback))
          .catch(err => errorLoading(err))
      },
    },
    {
      path: '/mwp/create_order',
      getComponent(location, callback) {
        import('./containers/MWP/CreateOrderContainer')
          .then(module => callback(null, isEnterpriseUser(enterpriseUserIsNotResetPassword(module.default))))
          .catch(err => errorLoading(err))
      },
    },
    {
      path: '/mwp/orders/:orderId',
      getComponent(location, callback) {
        import('./containers/MWP/WisePortOrderContainer')
          .then(module => callback(null, userIsAuthenticated(userIsNotPlatformAdminAdmin(enterpriseUserIsNotResetPassword(module.default)))))
          .catch(err => errorLoading(err))
      },
    },
    {
      path: '/mwp/orders_spike',
      getComponent(location, callback) {
        import('./containers/MWP/WisePortOrderContainer/OfferSelectionStep')
          .then(loadRoute(callback))
          .catch(err => errorLoading(err))
      },
    },
    {
      path: '/:orderType/orders/:orderId/payment_success',
      getComponent(location, callback) {
        import('./containers/CheckoutCounter/PaymentSuccessContainer') // eslint-disable-line block-scoped-var
          .then((module) => callback(null, userIsAuthenticated(enterpriseUserIsNotResetPassword(module.default))))
          .catch(err => errorLoading(err))
      },
    },
    {
      path: '/:orderType/orders/:orderId/checkout_counter',
      onEnter: checkIsNotResetPassword,
      getComponent(location, callback) {
        import('./containers/CheckoutCounter/CheckoutCounterContainer') // eslint-disable-line block-scoped-var
          .then((module) => callback(null, isEnterpriseUser(userIsAuthenticated(module.default))))
          .catch(err => errorLoading(err))
      },
    },
    {
      path: '/scf/create_order',
      getComponent(location, callback) {
        import('./containers/SCF/CreateSCFOrderContainer')
          .then((module) => callback(null, userIsAuthenticated(enterpriseUserIsNotResetPassword(module.default))))
          .catch(err => errorLoading(err))
      },
    },
    {
      path: '/scf/orders/:orderId',
      getComponent(location, callback) {
        import('./containers/SCF/SCFOrderContainer')
          .then((module) => callback(null, userIsAuthenticated(enterpriseUserIsNotResetPassword(module.default))))
          .catch(err => errorLoading(err))
      },
    },
    {
      path: '/not_found',
      getComponent(location, callback) {
        import('./containers/NotFound') // eslint-disable-line block-scoped-var
          .then((module) => callback(null, userIsAuthenticated(enterpriseUserIsNotResetPassword(module.default))))
          .catch(err => errorLoading(err))
      }
    },
    {
      path: '*',
      getComponent(location, callback) {
        import('./containers/NotFound') // eslint-disable-line block-scoped-var
          .then((module) => callback(null, userIsAuthenticated(enterpriseUserIsNotResetPassword(module.default))))
          .catch(err => errorLoading(err))
      }
    }
  ]
}
