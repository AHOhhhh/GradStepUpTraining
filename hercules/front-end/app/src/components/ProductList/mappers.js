import {get} from 'lodash'

const blankPlaceholder = '-'

export const mapProductInfo = (product, currency) => ({
  name: product.name,
  grossWeight: product.grossWeight || get(product, 'weight.value', 0),
  length: product.length || get(product, 'size.length', 0),
  width: product.width || get(product, 'size.width', 0),
  height: product.height || get(product, 'size.height', 0),
  unitPrice: product.unitPrice || product.price,
  currency: product.currency || currency,
  totalAmount: product.totalAmount || product.quantity || blankPlaceholder,
  packageAmount: product.packageAmount || product.packageQuantity || blankPlaceholder,
  description: product.description || product.declarationInfo || blankPlaceholder
})
