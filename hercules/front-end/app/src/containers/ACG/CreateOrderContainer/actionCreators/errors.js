export const validateCreateForm = (hasError) => (dispatch) => (
  dispatch({
    type: 'VALIDATE_ACG_CREATE_ORDER_FORM',
    hasError,
  })
)
