import * as types from '../../share/constants/index';

const initialState = {
  paymentStatus: 'notStartPayment',
  orderPriceDetail: {
    offers: [
      {
        offerId: 1,
        companyId: 123,
        companyName: '西安xxxxx代理服务公司',
        paymentDetails: [
          {
            itemName: 'export_declaration',
            amount: 4000
          },
          {
            itemName: 'import_clearance',
            amount: 4000
          },
        ],
      }
    ],
    totalPrice: 1000
  }
}

const paymentReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.SET_PAYMENT_STATUS:
        return {
          ...state,
          paymentStatus: action.status
        }
      case types.GET_ORDER_PRICE_DETAIL_SUCCESS:
        return {
          ...state,
          orderPriceDetail: action.req.data
        }
      default:
        return state
    }
  }

export default paymentReducer
