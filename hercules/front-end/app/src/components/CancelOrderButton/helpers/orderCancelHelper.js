import _ from 'lodash'

const constantsMap = {
  acg: ['Submitted', 'WaitForPay'],
  mwp: ['Submitted', 'TenderOffer'],
  wms: ['Submitted', 'Audited']
}

export const canCancel = ({ orderType, status }) => _.includes(constantsMap[orderType], status) && status !== 'Cancelled'
