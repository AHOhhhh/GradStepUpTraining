import * as types from '../share/constants'

export function setEnterpriseTabsKey(key) {
  return function (dispatch) {
    dispatch({
      type: types.ADMIN_SET_ENTERPRISE_TABS_KEY,
      key
    })
  }
}
