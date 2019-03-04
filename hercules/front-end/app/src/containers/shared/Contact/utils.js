import {isEmpty} from 'lodash'
import {concatArgs} from 'utils'

export const mapContacts = (contacts) => {
  if (!isEmpty(contacts)) {
    return contacts.map(item => Object.assign({}, item, {
      phone: concatArgs(', ', item.cellphone, item.telephone),
      allAddress: concatArgs('', item.country, item.province, item.city, item.district, item.address)
    }))
  }
  return contacts
}
