/* eslint-disable */
import {UserAuthWrapper as userAuthWrapper} from 'redux-auth-wrapper';
import {routerActions} from 'react-router-redux';

const extended = Object.assign || function(target) {
    for (var i = 1; i < arguments.length; i++) {
      var source = arguments[i];
      for (var key in source) {
        if (Object.prototype.hasOwnProperty.call(source, key)) {
          target[key] = source[key];
        }
      }
    }
    return target;
  };

const defaults = {
  redirectQueryParamName: 'redirect',
  locationSelector: function locationSelector(_ref) {
    const location = _ref.location;
    return location;
  },
};

const defaults$args = extended({}, defaults, {});
let redirectQueryParamName = defaults$args.redirectQueryParamName;
let locationSelector = defaults$args.locationSelector;

function getRedirectQueryParam(props) {
  let locationSelector = defaults$args.locationSelector;
  let location = locationSelector(props);
  return location.query[redirectQueryParamName];
};

function getLoginPathByUserRole(state) {
  if (state.routing.locationBeforeTransitions.pathname.indexOf('/admin/') > -1) {
    return '/admin/login'
  } else {
    return '/login'
  }
}

function getLandingPagePathByUserRole(state, ownProps) {
  if (state.routing.locationBeforeTransitions.pathname.indexOf('/admin/') > -1) {
    return getRedirectQueryParam(ownProps) || '/admin/orders'
  } else {
    return getRedirectQueryParam(ownProps) || '/'
  }
}

export const userIsAuthenticated = userAuthWrapper({
    authSelector: state => state,
    redirectAction: routerActions.replace,
    failureRedirectPath: (state, ownProps) => getLoginPathByUserRole(state),
    wrapperDisplayName: 'userIsAuthenticated',
    predicate: state => (state.auth.user !== undefined && state.auth.user !== null)
  });

export const userIsNotAuthenticated = userAuthWrapper({
  authSelector: state => state.auth,
  redirectAction: routerActions.replace,
  failureRedirectPath: (state, ownProps) => getLandingPagePathByUserRole(state, ownProps),
  allowRedirectBack: false,
  wrapperDisplayName: 'UserIsNotAuthenticated',
  predicate: auth => (auth.user === null || auth.user === undefined)
});

export const userIsNotEnterpriseAdmin = userAuthWrapper({
  authSelector: state => state.auth,
  redirectAction: routerActions.replace,
  failureRedirectPath: '/',
  allowRedirectBack: false,
  wrapperDisplayName: 'userIsNotEnterpriseAdmin',
  predicate: auth => (auth.user !== null) && (auth.user.role !== 'EnterpriseAdmin')
});

export const userIsNotPlatformAdmin = userAuthWrapper({
  authSelector: state => state.auth,
  redirectAction: routerActions.replace,
  failureRedirectPath: '/',
  allowRedirectBack: false,
  wrapperDisplayName: 'userIsNotPlatformAdmin',
  predicate: auth => (auth.user !== null) && (auth.user.role !== 'PlatformAdmin')
});

export const userIsNotPlatformAdminAdmin = userAuthWrapper({
  authSelector: state => state.auth,
  redirectAction: routerActions.replace,
  failureRedirectPath: '/',
  allowRedirectBack: false,
  wrapperDisplayName: 'userIsNotPlatformAdminAdmin',
  predicate: auth => (auth.user !== null) && (auth.user.role !== 'PlatformAdmin')
});

export const enterpriseUserIsNotResetPassword = userAuthWrapper({
  authSelector: state => state.auth,
  redirectAction: routerActions.replace,
  failureRedirectPath: '/change_password',
  allowRedirectBack: false,
  wrapperDisplayName: 'enterpriseUserIsNotResetPassword',
  predicate: auth => (auth.user !== null) && (auth.user.resettable)
});
/* eslint-enable */
