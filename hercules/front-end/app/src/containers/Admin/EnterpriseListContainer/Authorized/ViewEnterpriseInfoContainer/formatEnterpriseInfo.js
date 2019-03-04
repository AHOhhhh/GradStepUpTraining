import {map} from 'lodash'

const ENTERPRISE_ADMIN = {
  username: '用户名称',
  fullname: '联系人姓名',
  telephone: '联系方式',
  email: '邮箱'
}

const ENTERPRISE_INFO = {
  name: '企业名称',
  uniformSocialCreditCode: '统一社会信用代码',
  businessLicenseNumber: '营业执照编号',
  taxPayerNumber: '纳税人识别号',
  organizationCode: '组织机构代码',
  registrationAddress: '公司注册地址',
  artificialPersonName: '法人代表',
  artificialPersonContact: '法人联系电话'
}

const formatObject = (object, data) => {
  return map(object, (value, key) => {
    return {
      propName: value,
      value: data[key]
    }
  })
}

const formatEnterpriseData = (enterpriseAdmin, enterpriseInfo) => {
  const {telephone, cellphone} = enterpriseAdmin
  const phone = cellphone ? `${cellphone}${telephone ? '；' + telephone : ''}` : (telephone || '')
  const newEnterpriseAdmin = Object.assign({}, enterpriseAdmin, {telephone: phone})

  return [
    {
      title: '账号基本信息',
      items: formatObject(ENTERPRISE_ADMIN, newEnterpriseAdmin)
    }, {
      title: '企业资质信息',
      items: formatObject(ENTERPRISE_INFO, enterpriseInfo)
    }]
}

export default formatEnterpriseData