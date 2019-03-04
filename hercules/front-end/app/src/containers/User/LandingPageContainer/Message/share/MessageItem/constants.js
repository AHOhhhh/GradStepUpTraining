export const serviceType = {
  Declaration_Clearance: {
    name: '报关/清关',
    style: 'import-clearance',
  },
  Agent: {
    name: '进出口代理',
    style: 'export-agent'
  },
  Inspection_Declaration: {
    name: '报检',
    style: 'export-declaration'
  },
  PickUp: {
    name: '上门取货',
    style: 'export-declaration'
  },
  DropOff: {
    name: '机场派送',
    style: 'export-agent'
  },
  AirCargo: {
    name: '航空运输',
    style: 'import-clearance'
  },
  Renew: {
    name: '续费',
    style: 'export-declaration'
  },
  Open: {
    name: '开通',
    style: 'import-clearance'
  },
  Recharge: {
    name: '充值',
    style: 'export-agent'
  }
}

// export const serviceType = {
//   '报关/清关': {
//     name: '报关/清关',
//     style: 'import-clearance',
//   },
//   进出口代理: {
//     name: '进出口代理',
//     style: 'export-agent'
//   },
//   报检: {
//     name: '报检',
//     style: 'export-declaration'
//   },
//   上门取货: {
//     name: '上门取货',
//     style: 'export-declaration'
//   },
//   机场派送: {
//     name: '机场派送',
//     style: 'export-agent'
//   },
//   航空运输: {
//     name: '航空运输',
//     style: 'import-clearance'
//   },
//   续费: {
//     name: '续费',
//     style: 'export-declaration'
//   },
//   开通: {
//     name: '开通',
//     style: 'import-clearance'
//   },
//   充值: {
//     name: '充值',
//     style: 'export-agent'
//   }
// }

export const ORDER_TYPE_MAP = {
  mwp: '关务服务',
  acg: '航空货运',
  scf: '供应链金融',
  wms: '仓储管理',
}
