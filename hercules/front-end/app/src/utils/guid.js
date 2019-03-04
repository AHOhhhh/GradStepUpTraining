/* eslint-disable */
import jwtDecode from 'jwt-decode'
import {cookie} from 'utils'

export function guid() {
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
    s4() + '-' + s4() + s4() + s4();
}

function s4() {
  return Math.floor((1 + Math.random()) * 0x10000)
    .toString(16)
    .substring(1);
}


export function getAuthInfoFromToken(token) {
  let auth = {}
  const tokenString = token || cookie.get('TOKEN')
  const tokenObject = tokenString ? jwtDecode(tokenString.split(' ')[1]) : null
  if (tokenObject) {
    auth = {
      privileges: tokenObject.privileges.split(','),
      userId: tokenObject.userId,
      enterpriseId: tokenObject.enterpriseId,
      role: tokenObject.role,
      userName: tokenObject.userName
    }
  }
  return auth
}

export function isUserSessionExpired(token) {
  const tokenString = token || cookie.get('TOKEN')
  const tokenObject = tokenString ? jwtDecode(tokenString.split(' ')[1]) : null

  if(!tokenObject || !tokenObject.exp) {
    return false;
  }

  const expiredTime = new Date(tokenObject.exp * 1000);
  const now = new Date();

  return now.getTime() > expiredTime.getTime();
}
/* eslint-enable */