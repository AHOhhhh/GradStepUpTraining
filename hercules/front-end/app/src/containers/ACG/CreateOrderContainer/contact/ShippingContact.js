import React, { Component } from 'react'
import cssModules from 'react-css-modules'
import { find, isEmpty } from 'lodash'

import { PopModalList, PopModalEdit } from 'components'

import { createContact, updateContact } from 'actions'
import { onSelectShippingAddressId } from '../actionCreators/shippingInfo'
import styles from './index.module.scss'

const portMap = {
  departure: '始发港',
  arrival: '到达港',
}

class ShippingContact extends Component {
  state = {
    listVisible: false,
    addVisible: false,
    editVisible: false,
  }

  getNotice(port, airport) {
    return <span><b>*提示：</b>{`上门取货的服务范围仅在 ${portMap[port]}-`}<b>{airport.city}</b>{' 城市范围内'}</span>
  }

  displayModal(type, isVisible) {
    this.setState({
      ...this.state,
      [`${type}Visible`]: isVisible,
    })
  }

  render() {
    const contactTextMap = {
      departure: '取货地址',
      arrival: '送货地址'
    }

    const {dispatch, contacts, currentId, port, airport, addressId, enterpriseId} = this.props
    const address = find(contacts, {id: addressId}) || {}

    return (
      <div className={styles.contact}>
        <div styleName="shipping-contacts">
          <div styleName="contacts-title">
            <h5>{contactTextMap[port]}</h5>

            {!isEmpty(address) && (
              <span styleName="address-editor">
                <a onClick={() => this.displayModal('edit', true)}>编辑</a>
                <span> | </span>
                <a onClick={() => this.displayModal('list', true)}>重新选择</a>
              </span>
            )}
          </div>

          {!isEmpty(address)
            ? (<div>
              <p>{address.name}</p>
              <p>{address.phone}</p>
              <p>{address.allAddress}</p>
            </div>)
            : (<div styleName="contacts-content">
              <a onClick={() => this.displayModal('add', true)}>新增</a>
              <span> | </span>
              <a onClick={() => this.displayModal('list', true)}>从地址簿选择</a>
            </div>)}
        </div>

        <PopModalList
          title={'从地址簿选择联系方式'}
          notice={this.getNotice(port, airport)}
          handleSubmit={(contactId) => {
            dispatch(onSelectShippingAddressId(port, contactId))
            this.displayModal('list', false)
          }}
          onCancel={() => this.displayModal('list', false)}
          visible={this.state.listVisible}
          data={contacts || []}
          currentId={currentId || 0}
          width="800px"
        />

        <PopModalEdit
          title={'添加联系方式'}
          notice={this.getNotice(port, airport)}
          handleSubmit={(params) => {
            dispatch(createContact({...params, enterpriseId}, (newId) => {
              dispatch(onSelectShippingAddressId(port, newId))
              this.displayModal('add', false)
            }))
          }}
          regionLimit={airport.city}
          onCancel={() => this.displayModal('add', false)}
          visible={this.state.addVisible}
          width="800px"
        />

        <PopModalEdit
          title={'编辑联系方式'}
          notice={this.getNotice(port, airport)}
          handleSubmit={(params) => {
            dispatch(updateContact(addressId, {...params, enterpriseId}, enterpriseId, () => {
              this.displayModal('edit', false)
            }))
          }}
          regionLimit={airport.city}
          onCancel={() => this.displayModal('edit', false)}
          visible={this.state.editVisible}
          contact={address}
          width="800px"
        />
      </div>
    )
  }
}

export default cssModules(ShippingContact, styles, {allowMultiple: true})
