export const toData = (originalData) => {
  const chargingRules = originalData.chargingRules;
  chargingRules.forEach((rule) => {
    delete rule.editable;
    delete rule.key;
    delete rule.originalIndex;
    rule.unitPrice = parseFloat(rule.unitPrice);
    rule.quantityFrom = parseInt(rule.quantityFrom);
    rule.quantityTo = parseInt(rule.quantityTo);
  });
  originalData.chargingRules = chargingRules;
  originalData.serviceIntro = originalData.serviceIntro || '';
  originalData.effectiveFrom = originalData.effectiveFrom.format('YYYY-MM-DD');
  originalData.effectiveTo = originalData.effectiveTo.format('YYYY-MM-DD');
  originalData.currency = 'CNY';
  originalData.status = 'Audited';
  originalData.approvedPrice = originalData.approvedPrice.replace(/,/g, '');

  return originalData;
};

export const checkInteger = (number) => {
  return number.toString().match(/^\d+$/);
};

export const checkPositiveNumber = (number) => {
  return number.toString().match(/^[+]?([0-9]+(?:[.][0-9]*)?|\.[0-9]+)$/) && parseFloat(number) > 0;
};

export const checkPrice = (number) => {
  return number.toString().match(/^[0-9]+(\.[0-9]{2})?$/);
};

export const checkNonNegativeNumber = (number) => {
  return number.toString().match(/^\d+(\.{0,1}\d*){0,1}$/);
};

export const checkNumberAndLetter = (value) => {
  return value.toString().match(/^[a-z0-9]+$/i);
}
