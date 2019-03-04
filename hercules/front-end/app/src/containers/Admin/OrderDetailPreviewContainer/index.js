import React from 'react'

import SCFOrderContainer from '../../SCF/SCFOrderContainer'
import WisePortOrderContainer from '../../MWP/WisePortOrderContainer'
import WMSOrderContainer from '../../WMS/OrderContainer'
import ACGOrderManagementContainer from '../../ACG/OrderManagementContainer'

const MAP_ORDER_COMPONENT = {
  scf: SCFOrderContainer,
  mwp: WisePortOrderContainer,
  wms: WMSOrderContainer,
  acg: ACGOrderManagementContainer
}


const OrderDetailPreviewContainer = (props) => {
  const {orderType} = props.params
  const Container = MAP_ORDER_COMPONENT[orderType]
  return (<div>
    <Container {...props} preview/>
  </div>)
}

export default OrderDetailPreviewContainer