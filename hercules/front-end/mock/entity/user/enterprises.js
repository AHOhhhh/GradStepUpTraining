/* eslint-disable no-plusplus */
const uuid = require('uuid/v4')

const enterprisesInfo = [
  {
    artificialPersonContact: 'dd',
    artificialPersonName: 'userEU',
    businessLicenseNumber: '123455678901234',
    certificateForBusinessLicense: null,
    certificateForOrganization: null,
    certificateForTaxRegistration: null,
    certificateForUniformSocialCreditCode: 'myw3schoolsimage_1510748957050.jpg',
    comment: null,
    createdAt: '2017-11-15T12:29:28Z',
    createdBy: 'hello',
    enterpriseId: uuid(),
    lastModifiedAt: '2017-11-15T12:29:28Z',
    lastModifiedBy: 'hello',
    name: 'sfgsga',
    organizationCode: '1234567u-5',
    registrationAddress: 'ertyuio, tyuio',
    status: 'ENABLED',
    taxPayerNumber: '1234567890123456789t',
    uniformSocialCreditCode: '12345678901234567q',
    validationStatus: 'AuthorizationInProcess',
    admin: {
      id: '45795b5c-c778-4eda-b66b-8f6d3b04de2e',
      username: 'douqingqing',
      fullname: 'fh',
      email: '7343874332@qq.com',
      cellphone: '15829258737',
      telephone: '23454567576',
      status: null,
      enterpriseId: '51b23b71-2c14-4704-be8a-3fa096afd549',
      resettable: false,
      role: 'EnterpriseAdmin',
      createdAt: '2017-11-21T07:59:56Z',
      updatedAt: '2017-11-21T08:00:36Z',
      createdBy: '708ccc12-bd0c-3ba2-8754-c3e39a5dd62a',
      updatedBy: '45795b5c-c778-4eda-b66b-8f6d3b04de2e',
      privileges: [
        'EnterpriseUserManagementPrivilege'
      ]
    }
  },
  {
    artificialPersonContact: 'dd',
    artificialPersonName: 'userEU',
    businessLicenseNumber: '123455678901234',
    certificateForBusinessLicense: null,
    certificateForOrganization: null,
    certificateForTaxRegistration: null,
    certificateForUniformSocialCreditCode: 'myw3schoolsimage_1510748957050.jpg',
    comment: null,
    createdAt: '2017-11-15T12:29:28Z',
    createdBy: 'hello',
    enterpriseId: uuid(),
    lastModifiedAt: '2017-11-15T12:29:28Z',
    lastModifiedBy: 'hello',
    name: 'sfgsga',
    organizationCode: '1234567u-5',
    registrationAddress: 'ertyuio, tyuio',
    status: 'ENABLED',
    taxPayerNumber: '1234567890123456789t',
    uniformSocialCreditCode: '12345678901234567q',
    validationStatus: 'Authorized',
    admin: {
      id: '45795b5c-c778-4eda-b66b-8f6d3b04de2e',
      username: 'douqingqing',
      fullname: 'fh',
      email: '7343874332@qq.com',
      cellphone: '15829258737',
      telephone: '23454567576',
      status: null,
      enterpriseId: '51b23b71-2c14-4704-be8a-3fa096afd549',
      resettable: false,
      role: 'EnterpriseAdmin',
      createdAt: '2017-11-21T07:59:56Z',
      updatedAt: '2017-11-21T08:00:36Z',
      createdBy: '708ccc12-bd0c-3ba2-8754-c3e39a5dd62a',
      updatedBy: '45795b5c-c778-4eda-b66b-8f6d3b04de2e',
      privileges: [
        'EnterpriseUserManagementPrivilege'
      ]
    }
  },
  {
    artificialPersonContact: 'dd',
    artificialPersonName: 'userEU',
    businessLicenseNumber: '123455678901234',
    certificateForBusinessLicense: null,
    certificateForOrganization: null,
    certificateForTaxRegistration: null,
    certificateForUniformSocialCreditCode: 'myw3schoolsimage_1510748957050.jpg',
    comment: null,
    createdAt: '2017-11-15T12:29:28Z',
    createdBy: 'hello',
    enterpriseId: uuid(),
    lastModifiedAt: '2017-11-15T12:29:28Z',
    lastModifiedBy: 'hello',
    name: 'sfgsga',
    organizationCode: '1234567u-5',
    registrationAddress: 'ertyuio, tyuio',
    status: 'ENABLED',
    taxPayerNumber: '1234567890123456789t',
    uniformSocialCreditCode: '12345678901234567q',
    validationStatus: 'Unauthorized',
    admin: {
      id: '45795b5c-c778-4eda-b66b-8f6d3b04de2e',
      username: 'douqingqing',
      fullname: 'fh',
      email: '7343874332@qq.com',
      cellphone: '15829258737',
      telephone: '23454567576',
      status: null,
      enterpriseId: '51b23b71-2c14-4704-be8a-3fa096afd549',
      resettable: false,
      role: 'EnterpriseAdmin',
      createdAt: '2017-11-21T07:59:56Z',
      updatedAt: '2017-11-21T08:00:36Z',
      createdBy: '708ccc12-bd0c-3ba2-8754-c3e39a5dd62a',
      updatedBy: '45795b5c-c778-4eda-b66b-8f6d3b04de2e',
      privileges: [
        'EnterpriseUserManagementPrivilege'
      ]
    }
  }
]

module.exports = {
  getEnterpriseInfoByStatus(validationStatus) {
    for (const enterprise of enterprisesInfo) {
      if (enterprise.validationStatus === validationStatus) {
        return enterprise
      }
    }
  },
  getEnterprises: ({name, status, validationStatus, pageNum, pageSize}) => {

    let content = []

    for (let i = 0; i < 55; i++) {
      const firstEnterprise = {
        artificialPersonContact: 'dd',
        artificialPersonName: 'userEU',
        businessLicenseNumber: '123455678901234',
        certificateForBusinessLicense: null,
        certificateForOrganization: null,
        certificateForTaxRegistration: null,
        certificateForUniformSocialCreditCode: 'myw3schoolsimage_1510748957050.jpg',
        comment: null,
        createdAt: '2017-11-15T12:29:28Z',
        createdBy: 'hello',
        enterpriseId: uuid(),
        lastModifiedAt: '2017-11-15T12:29:28Z',
        lastModifiedBy: 'hello',
        name: '某知名企业',
        organizationCode: '1234567u-5',
        registrationAddress: 'ertyuio, tyuio',
        status: 'ENABLED',
        taxPayerNumber: '1234567890123456789t',
        uniformSocialCreditCode: '12345678901234567q',
        validationStatus: 'AuthorizationInProcess',
        admin: {
          id: '45795b5c-c778-4eda-b66b-8f6d3b04de2e',
          username: 'douqingqing',
          fullname: 'fh',
          email: '7343874332@qq.com',
          cellphone: '15829258737',
          telephone: '23454567576',
          status: null,
          enterpriseId: '51b23b71-2c14-4704-be8a-3fa096afd549',
          resettable: false,
          role: 'EnterpriseAdmin',
          createdAt: '2017-11-21T07:59:56Z',
          updatedAt: '2017-11-21T08:00:36Z',
          createdBy: '708ccc12-bd0c-3ba2-8754-c3e39a5dd62a',
          updatedBy: '45795b5c-c778-4eda-b66b-8f6d3b04de2e',
          privileges: [
            'EnterpriseUserManagementPrivilege'
          ]
        }
      }

      const secondEnterprise = {
        artificialPersonContact: 'dd',
        artificialPersonName: 'userEU',
        businessLicenseNumber: '123455678901234',
        certificateForBusinessLicense: null,
        certificateForOrganization: null,
        certificateForTaxRegistration: null,
        certificateForUniformSocialCreditCode: 'myw3schoolsimage_1510748957050.jpg',
        comment: null,
        createdAt: '2017-11-15T12:29:28Z',
        createdBy: 'hello',
        enterpriseId: uuid(),
        lastModifiedAt: '2017-11-15T12:29:28Z',
        lastModifiedBy: 'hello',
        name: '某不知名企业',
        organizationCode: '1234567u-5',
        registrationAddress: 'ertyuio, tyuio',
        status: 'ENABLED',
        taxPayerNumber: '1234567890123456789t',
        uniformSocialCreditCode: '12345678901234567q',
        validationStatus: 'AuthorizationInProcess',
        admin: {
          id: '45795b5c-c778-4eda-b66b-8f6d3b04de2e',
          username: 'douqingqing',
          fullname: 'fh',
          email: '7343874332@qq.com',
          cellphone: '15829258737',
          telephone: '23454567576',
          status: null,
          enterpriseId: '51b23b71-2c14-4704-be8a-3fa096afd549',
          resettable: false,
          role: 'EnterpriseAdmin',
          createdAt: '2017-11-21T07:59:56Z',
          updatedAt: '2017-11-21T08:00:36Z',
          createdBy: '708ccc12-bd0c-3ba2-8754-c3e39a5dd62a',
          updatedBy: '45795b5c-c778-4eda-b66b-8f6d3b04de2e',
          privileges: [
            'AuthorizationInProcess'
          ]
        }
      }

      const thirdEnterprise = {
        artificialPersonContact: 'dd',
        artificialPersonName: 'userEU',
        businessLicenseNumber: '123455678901234',
        certificateForBusinessLicense: null,
        certificateForOrganization: null,
        certificateForTaxRegistration: null,
        certificateForUniformSocialCreditCode: 'myw3schoolsimage_1510748957050.jpg',
        comment: null,
        createdAt: '2017-11-15T12:29:28Z',
        createdBy: 'hello',
        enterpriseId: uuid(),
        lastModifiedAt: '2017-11-15T12:29:28Z',
        lastModifiedBy: 'hello',
        name: 'sfgsga',
        organizationCode: '1234567u-5',
        registrationAddress: 'ertyuio, tyuio',
        status: 'ENABLED',
        taxPayerNumber: '1234567890123456789t',
        uniformSocialCreditCode: '12345678901234567q',
        validationStatus: 'Unauthorized',
        admin: {
          id: '45795b5c-c778-4eda-b66b-8f6d3b04de2e',
          username: 'douqingqing',
          fullname: 'fh',
          email: '7343874332@qq.com',
          cellphone: '15829258737',
          telephone: '23454567576',
          status: null,
          enterpriseId: '51b23b71-2c14-4704-be8a-3fa096afd549',
          resettable: false,
          role: 'EnterpriseAdmin',
          createdAt: '2017-11-21T07:59:56Z',
          updatedAt: '2017-11-21T08:00:36Z',
          createdBy: '708ccc12-bd0c-3ba2-8754-c3e39a5dd62a',
          updatedBy: '45795b5c-c778-4eda-b66b-8f6d3b04de2e',
          privileges: [
            'EnterpriseUserManagementPrivilege'
          ]
        }
      }

      const newEnterprises = [firstEnterprise, secondEnterprise, thirdEnterprise]

      content = content.concat(newEnterprises)
    }

    if (name) {
      content = content.filter(enterprise => enterprise.name.includes(name))
    }

    if (status) {
      content = content.filter(enterprise => enterprise.status === status)
    }

    if (validationStatus) {
      content = content.filter(enterprise => enterprise.validationStatus === validationStatus)
    }


    return {
      content,
      last: true,
      totalPages: (this.totalElements + this.pageSize - 1) / this.pageSize,
      totalElements: content.length,
      size: pageSize,
      number: pageNum,
      first: true,
      sort: null,
      numberOfElements: pageSize
    }
  }
}