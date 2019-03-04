import {get} from 'lodash'
import * as types from '../share/constants'

const validEnterprises = (state = [], action) => {
  switch (action.type) {
    case types.ADMIN_GET_VALID_ENTERPRISES_SUCCESS:
      return get(action, 'req.data.content', [])
    default:
      return state
  }

}

export default validEnterprises