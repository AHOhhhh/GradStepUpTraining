export const formatDefaultAddress = defaultAddress => ({
  contactId: defaultAddress.id,
  name: defaultAddress.name,
  address: defaultAddress.address,
  phone: defaultAddress.telephone,
  mobile: defaultAddress.cellphone,
  country: defaultAddress.countryAbbr,
  countryCode: defaultAddress.countryAbbr,
  province: defaultAddress.province,
  provinceCode: defaultAddress.provinceId,
  city: defaultAddress.city,
  cityCode: defaultAddress.cityId,
  district: defaultAddress.district,
  districtCode: defaultAddress.districtId
})
