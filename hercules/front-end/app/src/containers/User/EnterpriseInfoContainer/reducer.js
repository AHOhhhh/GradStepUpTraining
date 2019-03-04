import * as types from './constants'

export const initialState = {
  info: {}
}

const enterpriseVerifiedReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.GET_ENTERPRISE_INFO_FAIL:
        return state
      case types.GET_ENTERPRISE_INFO_SUCCESS:
        return Object.assign({}, state, {
          info: action.req.data
        })
      case types.GET_ATTACHMENT_IMAGE_SUCCESS:
        return Object.assign({}, state, {
          imageUrl: action.req.data
        })
      case types.GET_ATTACHMENT_IMAGE_FAIL:
        return Object.assign({}, state, {
          imageUrl: ''
        })
      case types.GET_ENTERPRISE_HISTORIES_SUCCESS: {
        return Object.assign({}, state, {
          comment: action.req.data[0].comment
        })
      }
      case types.CLEAR_ENTERPRISE_INFO: {
        return Object.assign({}, state, {
          info: {}
        })
      }
      default:
        return state
    }
  }

export default enterpriseVerifiedReducer
