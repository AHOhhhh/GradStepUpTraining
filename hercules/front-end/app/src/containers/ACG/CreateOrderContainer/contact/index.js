import React, {Component, PropTypes} from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import cssModules from 'react-css-modules'
import _ from 'lodash'

import * as action from 'actions'
import {PopModalList, PopModalEdit} from 'components'
import styles from './index.module.scss'

class Contact extends Component {
  static propTypes = {
    contacts: PropTypes.array,
    currentId: PropTypes.string,
    contact: PropTypes.object
  };

  constructor(props) {
    super(props);
    this.state = {
      listVisible: false,
      addVisible: false,
      editVisible: false
    }
  }

  getSelectedContact(contact) {
    if (!_.isEmpty(contact)) {
      return (<div styleName="row" key={contact.id}>
        <div styleName="cell">{contact.name}</div>
        <div styleName="cell">{contact.address}</div>
        <div styleName="cell">{this.concatPhones(contact.cellphone, contact.telephone)}</div>
        <div styleName="cell">
          <div styleName="sub-cell" onClick={() => this.displayModal('list', true)}>重选联系方式 |&nbsp;</div>
          <div styleName="sub-cell" onClick={() => this.displayModal('edit', true)}>编辑 |&nbsp;</div>
          <div styleName="sub-cell" onClick={() => this.props.actions.clearSelectedContact()}>清空</div>
        </div>
      </div>)
    }
    return (
      <div styleName="row operation" key={1}>
        <div styleName="more" onClick={() => this.displayModal('list', true)}>从地址簿选择 |</div>
        <div styleName="add" onClick={() => this.displayModal('add', true)}>&nbsp;新增联系方式</div>
      </div>
    )
  }

  displayModal(type, isVisible) {
    const modalStates = {
      list: {
        listVisible: isVisible
      },
      add: {
        addVisible: isVisible
      },
      edit: {
        editVisible: isVisible
      }
    };
    this.setState(modalStates[type])
  }

  concatPhones(cellphone, telephone) {
    return _.compact([cellphone, telephone]).join(', ')
  }

  render() {
    const selectedContact = this.props.contact;
    return (
      <div className={styles.contact}>
        <div styleName="contacts-box">
          <div styleName="table-like">
            {this.getSelectedContact(this.props.contact)}
          </div>
        </div>

        <PopModalList
          title={'更多联系人'}
          handleSubmit={(contactId) => {
            this.props.actions.selectContact(contactId);
            this.displayModal('list', false)
          }}
          onCancel={() => this.displayModal('list', false)}
          visible={this.state.listVisible}
          data={this.props.contacts}
          currentId={this.props.currentId}
          width="800px"
        />

        <PopModalEdit
          title={'新增联系方式'}
          handleSubmit={(params) => {
            this.props.actions.createContact(params);
            this.displayModal('add', false)
          }}
          onCancel={() => this.displayModal('add', false)}
          visible={this.state.addVisible}
          width="800px"
        />

        <PopModalEdit
          title={'编辑联系方式'}
          handleSubmit={(params) => {
            this.props.actions.updateContact(this.props.currentId, params);
            this.displayModal('edit', false)
          }}
          onCancel={() => this.displayModal('edit', false)}
          visible={this.state.editVisible}
          contact={selectedContact}
          width="800px"
        />
      </div>
    )
  }
}

export default (connect(
  state => ({
    contacts: state.contact.contacts,
    currentId: state.contact.currentId,
    contact: state.contact.contact
  }),
  dispatch => ({actions: bindActionCreators(action, dispatch)})
)(cssModules(Contact, styles, {allowMultiple: true})))
