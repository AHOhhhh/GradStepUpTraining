import _ from 'lodash'
import * as types from './constants'
import {mapContacts} from './utils';

export const initialState = {
  contacts: [],
  currentContact: {},
  currentId: null,
  contact: {},
  contact2: {}
}

const contactReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.GET_CONTACTS_REQUEST:
        return {
          ...state,
          status: 'loading',
          contacts: []
        }
      case types.GET_CONTACTS_SUCCESS:
        return Object.assign({
          ...state,
          status: 'success',
          contacts: mapContacts(action.req.data)
        })
      case types.GET_CONTACTS_FAILURE:
        return {
          ...state,
          status: 'failed',
          contacts: []
        }
      case types.SELECT_CONTACT:
        return Object.assign({}, state, {
          currentId: action.id,
          contact: _.find(state.contacts, (item) => (item.id === action.id))
        })
      case types.SELECT_CONTACT_FOR_FIRST:
        return Object.assign({}, state, {
          contact: action.contact
        })
      case types.SELECT_CONTACT_SECOND:
        return Object.assign({}, state, {
          currentId: action.id,
          contact2: _.find(state.contacts, (item) => (item.id === action.id))
        })
      case types.SELECT_CONTACT_FOR_SECOND:
        return Object.assign({}, state, {
          contact2: action.contact
        })
      case types.CREATE_CONTACTS:
        return Object.assign({}, state, {
          currentId: null,
        })
      case types.CREATE_CONTACTS_SUCCESS:
        return Object.assign({}, state, {
          currentId: _.last(action.req.headers.location.split('/'))
        })
      case types.CREATE_CONTACTS_FAILURE:
        return Object.assign({}, state, {
          currentId: null
        })
      // case types.UPDATE_CONTACTS_SUCCESS:
      //   return Object.assign({}, state, {})
      case types.UPDATE_CONTACTS_FAILURE:
        return Object.assign({}, state, {})
      case types.CLEAR_CONTACT:
        return Object.assign({}, state, {currentId: null, contact: {}})
      case types.CLEAR_CONTACT_SECOND:
        return Object.assign({}, state, {currentId: null, contact2: {}})
      default:
        return state
    }
  }

export default contactReducer
