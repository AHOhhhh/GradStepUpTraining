const operations = [
  {
    "id": "50c4d61f-dcba-44dc-b5f8-2ff1eeec4a1f",
    "enterpriseId": "e8209b6c-aa65-4bea-b14c-f6d0a4c81767",
    "enterpriseName": "baidu",
    "operatorName": "Test PlatformAdmin",
    "operatorRole": "PlatformAdmin",
    "index": 0,
    "type": "ResetPassword",
    "typeDescription": "重置企业admin密码",
    "createdAt": "2018-01-07T06:36:58Z",
    "updatedAt": "2018-01-07T09:36:58Z",
    "createdBy": "a5704243-d20e-4a32-9b0b-49c92d3bc41f",
    "updatedBy": "a5704243-d20e-4a32-9b0b-49c92d3bc41f"
  },{
    "id": "50c4d61f-dcba-44dc-b5f8-2ff1eeec4a1f",
    "enterpriseId": "e8209b6c-aa65-4bea-b14c-f6d0a4c81767",
    "enterpriseName": "baidu",
    "operatorName": "Test PlatformAdmin",
    "operatorRole": "PlatformAdmin",
    "index": 1,
    "type": "ResetPassword",
    "typeDescription": "重置企业admin密码",
    "createdAt": "2018-01-07T06:36:58Z",
    "updatedAt": "2018-01-07T09:36:58Z",
    "createdBy": "a5704243-d20e-4a32-9b0b-49c92d3bc41f",
    "updatedBy": "a5704243-d20e-4a32-9b0b-49c92d3bc41f"
  },{
    "id": "50c4d61f-dcba-44dc-b5f8-2ff1eeec4a1f",
    "enterpriseId": "e8209b6c-aa65-4bea-b14c-f6d0a4c81767",
    "enterpriseName": "baidu",
    "operatorName": "Test PlatformAdmin",
    "operatorRole": "PlatformAdmin",
    "index": 2,
    "type": "ResetPassword",
    "typeDescription": "重置企业admin密码",
    "createdAt": "2018-01-07T06:36:58Z",
    "updatedAt": "2018-01-07T09:36:58Z",
    "createdBy": "a5704243-d20e-4a32-9b0b-49c92d3bc41f",
    "updatedBy": "a5704243-d20e-4a32-9b0b-49c92d3bc41f"
  },{
    "id": "50c4d61f-dcba-44dc-b5f8-2ff1eeec4a1f",
    "enterpriseId": "e8209b6c-aa65-4bea-b14c-f6d0a4c81767",
    "enterpriseName": "baidu",
    "operatorName": "Test PlatformAdmin",
    "operatorRole": "PlatformAdmin",
    "index": 3,
    "type": "ResetPassword",
    "typeDescription": "重置企业admin密码",
    "createdAt": "2018-01-07T06:36:58Z",
    "updatedAt": "2018-01-07T09:36:58Z",
    "createdBy": "a5704243-d20e-4a32-9b0b-49c92d3bc41f",
    "updatedBy": "a5704243-d20e-4a32-9b0b-49c92d3bc41f"
  },
]

module.exports = {
  getOperationsByPage: (params) => {
    const page = params.page
    const size = params.size
    const content = operations.slice(page * size, ((page * size) + size))

    const operation = {
      "content": content,
      "size": size,
      "totalPages": 2,
      "first": false,
      "last": false,
      "number": 1,
      "numberOfElements": 10,
      "totalElements": 4
    }
    return operation
  }
}