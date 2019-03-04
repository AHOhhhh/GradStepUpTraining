import httpClient from 'utils/http'
import * as types from './constants'

export const getAdminOperationHistories = (enterpriseId, pageSize, pageNum) => ({
  type: types.ADMIN_GET_ADMIN_OPERATION_HISTORIES,
  promise: httpClient.get(`/user-operations?role=EnterpriseAdmin&enterpriseId=${enterpriseId}&size=${pageSize}&page=${pageNum}`)
})