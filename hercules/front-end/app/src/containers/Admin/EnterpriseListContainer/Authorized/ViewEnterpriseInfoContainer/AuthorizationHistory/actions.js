import httpClient from 'utils/http'
import * as types from './constants'

export const getEnterpriseHistories = (enterpriseId) => ({
  type: types.ADMIN_GET_ENTERPRISE_HISTORIES,
  promise: httpClient.get(`/enterprises/histories/${enterpriseId}`)
})

export const getAdminName = (adminId) => ({
  type: types.ADMIN_GET_ADMIN,
  promise: httpClient.get(`/users/${adminId}`)
})