const user = {
               "cellphone": "string",
               "createdAt": "2017-12-05T07:41:29.671Z",
               "createdBy": "string",
               "email": "string",
               "enterpriseId": "string",
               "fullname": "string",
               "id": "03b3684b-20be-4f0a-99fe-f7ae2f7cb5fb",
               "privileges": [
                 {
                   "createdAt": "2017-12-05T07:41:29.671Z",
                   "createdBy": "string",
                   "description": "string",
                   "id": 0,
                   "type": "AllPrivileges",
                   "updatedAt": "2017-12-05T07:41:29.671Z",
                   "updatedBy": "string"
                 }
               ],
               "resettable": true,
               "role": {
                 "createdAt": "2017-12-05T07:41:29.671Z",
                 "createdBy": "string",
                 "description": "string",
                 "id": 0,
                 "privileges": [
                   {
                     "createdAt": "2017-12-05T07:41:29.671Z",
                     "createdBy": "string",
                     "description": "string",
                     "id": 0,
                     "type": "AllPrivileges",
                     "updatedAt": "2017-12-05T07:41:29.672Z",
                     "updatedBy": "string"
                   }
                 ],
                 "type": "PlatformService",
                 "updatedAt": "2017-12-05T07:41:29.672Z",
                 "updatedBy": "string"
               },
               "status": "ENABLED",
               "telephone": "string",
               "updatedAt": "2017-12-05T07:41:29.672Z",
               "updatedBy": "string",
               "username": "string"
             }

module.exports = {
  getUserById: function () {
    return user;
  }
};