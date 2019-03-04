// 汉字，英文
export const REG_ZH_EN = /^[\u4E00-\u9FA5a-zA-Z]+$/
// 汉字，英文，数字，() （）.
export const REG_ZH_EN_NUM_EXTEND_1 = /^[\u4E00-\u9FA5a-zA-Z0-9（）().]+$/
// 汉字，英文，数字，() （）
export const REG_ZH_EN_NUM_EXTEND_2 = /^[\u4E00-\u9FA5\sa-zA-Z0-9（）()-]+$/
// 数字和特殊字符（）、-_/
export const REG_PHONE_EXTEND_3 = /^[0-9、\-_（）().]+$/
// 数字，字母，特殊字符
export const REG_NUM_EN_EXTEND = /^[a-zA-Z0-9、/-_（）().]+$/