import {sortBy} from 'lodash';

const ORDER_TYPES_MAP = {
  wms: 'WMS',
  acg: '航空货运',
  mwp: '口岸报关',
  scf: '供应链金融'
};

const PAY_METHODS_MAP = {
  online: '线上支付',
  offline: '线下支付',
  deferment: '后付'
};

export const orderTypesMap = (orderType) => {
  return ORDER_TYPES_MAP[orderType.toLowerCase()] || orderType;
};

export const payMethodsMap = (payMethod) => {
  return PAY_METHODS_MAP[payMethod.toLowerCase()] || payMethod;
};

export const orderBusiness = (payMethods) => {
  payMethods = payMethods.filter((item) => {
    return item.orderType !== 'ltp'
  })
  return sortBy(payMethods, (payMethod) => { return payMethod.orderType });
};

export const sortByPaymentMethod = (payMethods) => {
  return sortBy(payMethods, (payMethod) => { return payMethod.value }).reverse();
};

export const formatCheckboxesData = (data, originalPayMethods) => {
  const mergedResult = originalPayMethods.map((payMethod) => {
    const payMethods = data[payMethod.orderType];

    payMethod.payMethods.forEach((payMethodItem) => {
      if (!payMethodItem.editable && payMethodItem.enabled) {
        payMethods.push(payMethodItem.name);
      }
    });

    return {[payMethod.orderType]: payMethods};
  });

  const result = {};
  mergedResult.forEach((item) => {
    Object.assign(result, item);
  });

  return result;
};
