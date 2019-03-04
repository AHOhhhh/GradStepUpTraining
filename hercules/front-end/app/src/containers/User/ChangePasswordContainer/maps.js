import * as constants from './constants';

export const steps = {
  true: 2,
  false: 0
}

export const errorMessage = {
  'invalid captcha': constants.CAPTCHA_ERROR_MESSAGE,
  'original password wrong!': constants.ACCOUNT_ERROR_MESSAGE,
  'current user already init password!': constants.PASSWORD_RESETED,
  'invalid password': constants.INVALID_PASSWORD
}