# Admin

## 编辑订单

- 平台管理员更新WMS订单：

```
post(`/wms/orders/${orderId}`, params)

```

## 企业信息

- 管理员获取企业信息：

```
get('/enterprises/' + id)
```

- 管理员获取附件图片：

```
get('/file/pictures/' + file, {responseType: 'arraybuffer'})
```


- 管理员获取企业管理员信息：

```
get('/enterprises/' + id + '/admin')
```

## 企业列表

- 重置密码

```
post(`/enterprise-admin/${userId}/reset-password`);

```
- 更新状态

```
post('/enterprises/{enterpriseId}/status', {status: newStatus})

```

## 资金

- 获取资金

```
get('/web/transactions', {params})

```

## 登陆

- 登陆

```
post('/admin/login', Object.assign({}, params, redirect))

```

## 对账单

- 所有账单

```
get('/order-bill', {params})
```
- 某个账单

```
get(`/order-bill/${orderId}`)
```
- 导入

```
post('/order-bill/excel', formData)
```

- 导出

```
get('/order-bill/excel', formData)
```

## 操作记录

- 用户操作记录

```
get('/user-operations', {params})
```
- 订单操作记录

```
get('/order-operations', {params})
```

## 订单列表

- 支付资金

```
post('/payment-transactions/payment-status/online', {
          paymentId,
          sync: true
        })
```
- 所有订单

```
get('/orders', { params })
```
- 某订单

```
get('/orders/{orderId}')
```
- 取消订单

```
post(`/${order.orderType}/orders/${order.id}/cancellation`, params)
```

# WMS
 
## 创建

- 获取模版

```
get('/resources/wms/order-description.json')
```
- 创建

```
post('/wms/orders', params)
```
- 获取订单

```
get('/wms/orders?enterpriseId=' + params.enterpriseId)
```

- 某个订单

```
get('/wms/orders/' + orderId)
```

# 口岸报关

## 创建

- 口岸信息

```
get('/dictionary?code=ports')
```
- 监管信息

```
get('/dictionary?code=supervisionType')
```
- 运输方式

```
get('/dictionary?code=transportType')
```

## 订单

- 获取订单

```
post('/mwp/orders')
```

- 某个订单

```
get(`/mwp/orders/${orderId}`)
```

## 服务

- 服务产品

```
get(`/mwp/orders/${id}/suborders`)
```

## 选择仓库

- 获取仓库

```
get(`/mwp/orders/${orderId}/offers`)
```
- 更新

```
post(`/mwp/orders/${orderId}/selectedOffers`, {...data})
```
## 支付

- 获取价格信息

```
get(`/mwp/orders/${orderId}/suborders/price-detail`)
```
- 确认价格

```
post(`/mwp/orders/${orderId}/suborders/confirming-price`, data)
```

# SCF

## 创建

- 创建

```
post('/scf/orders', order)
```

## 获取

- 所有订单

```
get(`/scf/orders`)
```
- 某个订单

```
get(`/scf/orders/${orderId}`)
```

```
get(`/scf/orders/${orderId}/next`)
```

# 关务服务

## 订单

- 获取机场

```
get('/acg-api/airports')
```
- 获取订单

```
post('/acg/orders', order)
```

```
post('/acg/orders/{orderId}')
```

- 获取订单价格

```
post('/acg-api/order-price', params)
```

# 结算相关

## 支付

- 获取支付信息

```
get(`/orders/${orderType}/payment-methods`)
```

- 处理在线支付

```
post('/order-payment/online', orderInfo）
```
- 处理线下支付

```
post('/order-payment/offline', offlinePaymentInfo)
```

- 订单价格

```
get(`/orders/${orderId}/payment-requests`)
```

- 处理状态

```
post('/payment-transactions/payment-status/online', {
      paymentId
    })
```

- 线下支付审计

```
get(`/orders/${orderId}/transactions/latest-transaction`)
```

# 其他

## 联系人

- 获取联系人

```
get('/contacts?enterprise_id=' + enterpriseId)
```

- 创建联系人

```
post('/contacts', params)
```

- 删除联系人

```
post('/contacts/' + id + 'deletion')
```
- 更新联系人

```
post('/contacts/' + id, params)
```

## 物流信息查询

- 获取订单（需要验证码）

```
get(`/acg/orders?ids=${orderIds}&captchaId=${captchaId}&captcha=${captcha}`)
```
## 授权

- 授权

```
post(`/oauth2/authorize?client_id=${query.client_id}&redirect_uri=${encodeURI(query.redirect_uri)}&scope=${SCOPE}&state=${query.state}&response_type=code`)
```
