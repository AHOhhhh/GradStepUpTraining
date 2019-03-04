const initState = {
  isValidating: false,
  hasError: false,
}

const errorsReducer = (errors = initState, action) => {
  switch (action.type) {
    case 'VALIDATE_ACG_CREATE_ORDER_FORM':
      return {
        ...errors,
        isValidating: true,
        hasError: action.hasError
      }
    case 'CLEAR_ACG_ORDER_CREATED_INFO':
      return initState
    default:
      return errors
  }
}

export default errorsReducer;
