import {USER_ROLE} from 'constants'

const NAV_ITEMS = [
  {
    label: '工作台',
    className: 'icon-workspace',
    route: '/',
    role: [USER_ROLE.enterpriseUser]
  },
  {
    label: '订单管理',
    className: 'icon-orders',
    route: '/orders',
    role: [USER_ROLE.enterpriseUser]
  },
  {
    label: '联系方式管理',
    className: 'icon-contact',
    route: '/contact',
    role: [USER_ROLE.enterpriseUser]
  },
  {
    label: '信息查看',
    className: 'icon-enterprise',
    route: '/enterprise_info',
    role: [USER_ROLE.enterpriseUser]
  },
  {
    label: '用户管理',
    className: 'icon-user',
    route: '/',
    role: [USER_ROLE.enterpriseAdmin]
  },
  {
    label: '企业信息',
    className: 'icon-enterprise',
    route: '/enterprise_info',
    role: [USER_ROLE.enterpriseAdmin]
  },
  {
    label: '订单管理',
    className: 'icon-orders',
    route: '/admin/orders',
    role: [USER_ROLE.platformAdmin, USER_ROLE.platformAdmin]
  },
  {
    label: '企业审核管理',
    className: 'icon-enterprise',
    route: '/admin/enterprise_list',
    role: [USER_ROLE.platformAdmin]
  },
  {
    label: '资金流水',
    className: 'icon-money',
    route: '/admin/funds',
    role: [USER_ROLE.platformAdmin]
  },
  {
    label: '操作记录',
    className: 'icon-records',
    route: '/admin/operation_records',
    role: [USER_ROLE.platformAdmin]
  },
  {
    label: '对账单',
    className: 'icon-bill',
    route: '/admin/order_bills',
    role: [USER_ROLE.platformAdmin, USER_ROLE.platformAdmin]
  }
]

export default NAV_ITEMS