import { find, isFunction } from 'lodash'
import httpClient from '../../../utils/http'
import * as types from './constants'

export function selectContact(id) {
  return function (dispatch) {
    dispatch({
      type: types.SELECT_CONTACT,
      id
    })
  }
}

export function getContacts(enterpriseId) {
  return function (dispatch) {
    dispatch({
      type: types.GET_CONTACTS,
      promise: httpClient.get('/contacts?enterprise_id=' + enterpriseId)
    })
  }
}

export function selectContactByIdFor(id, target) {
  return function (dispatch) {
    dispatch({
      type: target === 1 ? types.SELECT_CONTACT : types.SELECT_CONTACT_SECOND,
      id
    })
  }
}

export function clearContactFor(target) {
  return function (dispatch) {
    dispatch({
      type: target === 1 ? types.CLEAR_CONTACT : types.CLEAR_CONTACT_SECOND
    })
  }
}

export function getContactsAndSelectFor(id, enterpriseId, target = 1) {
  return function (dispatch) {
    dispatch({
      type: types.GET_CONTACTS,
      promise: httpClient.get('/contacts?enterprise_id=' + enterpriseId).then(
        (data) => {
          dispatch({
            type: target === 1 ? types.SELECT_CONTACT_FOR_FIRST : types.SELECT_CONTACT_FOR_SECOND,
            contact: find(data.data, (item) => (item.id === id))
          });
          return data
        }
      )
    })
  }
}

export const createContact = (params, callback) => {
  return function (dispatch) {
    dispatch({
      type: types.CREATE_CONTACTS,
      promise: httpClient.post('/contacts', params).then(
        (contact) => {
          dispatch({
            type: types.GET_CONTACTS,
            promise: httpClient.get(`/contacts?enterprise_id=${params.enterpriseId}`)
          }).then((data) => {
            callback(contact.data)
            return data
          })
          return contact
        }
      )
    })
  }
}

export function deleteContact(id, enterpriseId, callback) {
  return function (dispatch) {
    dispatch({
      type: types.DELETE_CONTACTS,
      promise: httpClient.post('/contacts/' + id + '/deletion').then(
        (result) => {
          callback()
          dispatch({
            type: types.GET_CONTACTS,
            promise: httpClient.get(`/contacts?enterprise_id=${enterpriseId}`)
          }).then((data) => {
            return data
          })
          return result
        }
      )
    })
  }
}

export function createContactDual(params, target, enterpriseId) {
  return function (dispatch) {
    dispatch({
      type: types.CREATE_CONTACTS,
      promise: httpClient.post('/contacts', params).then(
        (data) => {
          dispatch(getContactsAndSelectFor(data.data, enterpriseId, target));
          return data;
        }
      )
    })
  }
}

export function updateContact(id, params, enterpriseId, callback = null) {
  return function (dispatch) {
    dispatch({
      type: types.UPDATE_CONTACTS,
      promise: httpClient.post('/contacts/' + id, params).then(
        (data) => {
          dispatch(getContactsAndSelectFor(id, enterpriseId));
          if (isFunction(callback)) {
            callback()
          }
          return data
        }
      )
    })
  }
}

