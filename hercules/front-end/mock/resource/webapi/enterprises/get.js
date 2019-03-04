const data = require('entity/user/enterprises')

module.exports = function (req, res, next) {
  // res.sendStatus(404);
  const {name, status, validationStatus, pageNum, pageSize} = req.params
  res.send(200, data.getEnterprises({name, status, validationStatus, pageNum, pageSize}));
  next();
};
