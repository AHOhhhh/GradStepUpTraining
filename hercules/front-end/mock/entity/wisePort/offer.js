module.exports = {
  offerId: 1,
  importOrExport: 1,
  companyId: 123,
  companyName: '西安XXX代理服务公司',
  settlement: '按订单结算',
  typeOfPayment: '先服务后付款',
  offerCost: '含稅价',
  contactNumber: '400-890-0505',
  offerValidationTime: '2017-10-10 10:00:11',
  services: [
    'export_agent'
  ],
  items: [
    {
      goods: [
        {
          name: '航材2',
          hsCode: 'G_HC2',
          status: '报关单放行',
          volume: '100',
          currency: null,
          customsId: '102390120312',
          netWeight: '900',
          unitPrice: 500000,
          grossWeight: '1000',
          totalAmount: '500',
          packageAmount: 5
        }
      ],
      currency: 'CNY',
      itemName: '清关费',
      itemType: '一口价',
      estimation: 999,
      exactPrice: 100,
      description: '',
      priceDescription: null
    }
  ]
}