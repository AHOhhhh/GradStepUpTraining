export const paymentTypeMap = {
  personal_creditCard: 'LARGE_CREDIT_CARD',
  personal_debitCard: 'BANK_B2C',
  enterprise_debitCard: 'BANK_B2B'
}

export const PAYMENT_MODE_MAP = {
  personal_debitCard: 14,
  personal_creditCard: 17,
  enterprise_debitCard: 8
}

export const mapModalContent = {
  unCheckout: '正在支付，请稍等...',
  checking: '未收到实际支付款，请继续支付。'
}

export const paymentMethodMap = {
  personal: 'ONLINE',
  enterprise: 'ONLINE',
  offline: 'OFFLINE'
}

export const defaultPaymentMethodMap = {
  ONLINE: 'personal',
  OFFLINE: 'offline'
}