module.exports = function (req, res, next) {
  const supportedDistricts = require('entity/air-cargo/supported-districts');

  res.send(200, supportedDistricts);
  next();
}
