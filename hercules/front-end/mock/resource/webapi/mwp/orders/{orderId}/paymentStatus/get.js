module.exports = function (req, res, next) {
  res.send(200, {
    paymentStatus: 'failure'
  });
  next();
}
