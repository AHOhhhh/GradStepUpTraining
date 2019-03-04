export const USER_ROLE = {
  enterpriseAdmin: 'EnterpriseAdmin',
  enterpriseUser: 'EnterpriseUser',
  platformAdmin: 'PlatformAdmin'
}

export const USER_ROLE_NAME_MAP = {
  EnterpriseAdmin: '企业管理员',
  EnterpriseUser: '企业用户',
  PlatformAdmin: '平台管理员'
}

export const EnterpriseUsers = [
  'EnterpriseAdmin',
  'EnterpriseUser'
]

export const AUTHENTICATION_ERROR_MAP = {
  31012: '用户名或密码错误',
  31013: '验证码错误或失效',
  31014: '无效的用户信息',
  31015: 'SESSION过期',
  31016: '当前用户已被禁用',
  31017: '当前用户无此权限',
  31018: '无效的用户',
  31019: '未知错误'
}

export const FORBIDDEN_ERROR_MAP = {
  10004: '当前用户无此权限',
  // 30022: '当前用户无此权限'
}

export const SERVER_ERROR = '服务端错误, 请稍后再试'