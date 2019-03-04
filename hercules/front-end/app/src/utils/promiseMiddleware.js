const status = {
  loading: false,
  success: false
}

export function promiseMiddleware() {
  return next => action => {
    const {promise, type, ...rest} = action
    if (!promise) return next(action)
    const REQUEST = type + '_REQUEST'
    const SUCCESS = type + '_SUCCESS'
    const FAILURE = type + '_FAILURE'
    next({...rest, type: REQUEST, status: {...status, loading: true}})
    return promise
      .then(req => {
        next({...rest, req, type: SUCCESS, status: {...status, success: true}})
        return Promise.resolve(...rest, req)
      })
      .catch(error => {
        next({...rest, error, type: FAILURE, status})
        return Promise.reject(error)
      })
  }
}
