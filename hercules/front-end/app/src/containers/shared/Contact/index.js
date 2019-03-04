import React, {Component, PropTypes} from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import cssModules from 'react-css-modules'
import _ from 'lodash'
import * as action from 'actions'
import {Radio} from 'antd';
import {PopModalList, PopModalEdit, PopModalClear} from 'components'
import {concatArgs} from '../../../utils/format'
import styles from './index.module.scss'

class Contact extends Component {
  static propTypes = {
    contacts: PropTypes.array,
    currentId: PropTypes.string,
    contact: PropTypes.object,
    contact2: PropTypes.object,
    contactNum: PropTypes.string,
    roles: PropTypes.array,
    mode: PropTypes.string,
    contactView: PropTypes.object
  };

  static defaultProps = {
    contactNum: 'single',
    roles: [],
    mode: 'edit'
  };

  constructor(props) {
    super(props);
    this.state = {
      listVisible: false,
      addVisible: false,
      editVisible: false,
      clearVisible: false,
      current: 1
    }
  }

  componentDidMount() {
    this.props.actions.getContacts(this.props.enterpriseId)
  }

  componentWillReceiveProps(nextProps) {
    if (!_.isEmpty(nextProps.contacts) && _.isEmpty(nextProps.contact)) {
      const defaultContact = nextProps.contacts.filter(item => item.default)[0];
      if (defaultContact) {
        this.props.actions.selectContactByIdFor(defaultContact.id, this.state.current)
      } else {
        this.props.actions.selectContactByIdFor(nextProps.contacts[0].id, this.state.current)
      }
    }
  }

  displayContactView(contact) {
    return (
      (<div styleName="table-like">
        <div styleName="row" key={contact.id}>
          <div styleName="cell"><Radio checked/></div>
          <div styleName="cell">{contact.name}</div>
          <div styleName="cell">{
            concatArgs('', contact.country, contact.province, contact.city, contact.district, contact.address)
          }</div>
          <div styleName="cell">{concatArgs(', ', contact.cellphone, contact.telephone)}</div>

        </div>
      </div>)
    )
  }

  displayContact(contacts, contact, target) {
    if (_.isEmpty(contacts)) {
      return (
        <div styleName="table-like">
          <div styleName="row" key={1}>
            <div styleName="cell">请添加联系方式</div>
          </div>
        </div>
      )
    }

    if (!contacts.find((item) => item.id === contact.id)) {
      contact = contacts[0]
    }

    return (
      (<div styleName="table-like">
        <div styleName="row" key={contact.id}>
          <div styleName="cell"><Radio checked/></div>
          <div styleName="cell">{contact.name}</div>
          <div styleName="cell">{
            concatArgs('', contact.country, contact.province, contact.city, contact.district, contact.address)
          }</div>
          <div styleName="cell">{concatArgs(', ', contact.cellphone, contact.telephone)}</div>
          {
            this.props.mode !== 'view' &&
            <div styleName="cell">
              <span styleName="sub-cell" onClick={() => this.displayModal('edit', true, target)}>编辑</span>
            </div>
          }
        </div>
      </div>)
    )
  }

  displayModal(type, isVisible, target) {
    const modalStates = {
      list: {
        listVisible: isVisible,
        current: target
      },
      add: {
        addVisible: isVisible,
        current: target
      },
      edit: {
        editVisible: isVisible,
        current: target
      },
      clear: {
        clearVisible: isVisible,
        current: target
      }
    };
    this.setState(modalStates[type])
  }

  render() {
    return (
      this.props.mode === 'view' ?
        <div className={styles.contact}>
          <div styleName="box-title" className="box-title">联系方式</div>
          {
            this.props.roles[0] !== undefined &&
            <div styleName="contact-role">{this.props.roles[0]}</div>
          }
          <div styleName="contacts-box" className="contacts-box">
            {this.displayContactView(this.props.contactView)}
          </div>
        </div> :

        <div className={styles.contact}>
          <div styleName="box-title" className="box-title">联系方式</div>
          {
            this.props.roles[0] !== undefined &&
            <div styleName="contact-role">{this.props.roles[0]}</div>
          }
          <div styleName="contacts-box" className="contacts-box">
            {this.displayContact(this.props.contacts, this.props.contact, 1)}
          </div>
          <div styleName="operation" className="operation" key={1}>
            <span styleName="more" onClick={() => this.displayModal('list', true, 1)}>更多联系方式</span>
            <span styleName=""> | </span>
            <span styleName="add" onClick={() => this.displayModal('add', true, 1)}>新增联系方式</span>
          </div>

          {
            this.props.contactNum === 'dual' &&
            <div styleName="contact-role">{this.props.roles[1]}</div>
          }
          {
            this.props.contactNum === 'dual' &&
            <div styleName="contacts-box">
              <div styleName="table-like">
                {this.displayContact(this.props.contacts, this.props.contact2, 2)}
              </div>
            </div>
          }

          <PopModalList
            title={'地址簿联系方式'}
            handleSubmit={(contactId) => {
              this.props.actions.selectContactByIdFor(contactId, this.state.current);
              this.displayModal('list', false)
            }}
            onCancel={() => this.displayModal('list', false)}
            visible={this.state.listVisible}
            data={this.props.contacts}
            currentId={this.props.contact.id}
            width="800px"
          />

          <PopModalEdit
            title={'新增联系方式'}
            handleSubmit={(params) => {
              this.props.actions.createContactDual(params, this.state.current, this.props.enterpriseId);
              this.displayModal('add', false, this.state.current)
            }}
            onCancel={() => this.displayModal('add', false, this.state.current)}
            visible={this.state.addVisible}
            enterpriseId={this.props.enterpriseId}
            width="700px"
          />

          <PopModalEdit
            title={'编辑联系方式'}
            handleSubmit={(params) => {
              const id = this.state.current === 1 ? this.props.contact.id : this.props.contact2.id;
              this.props.actions.updateContact(id, params, this.props.enterpriseId);
              this.displayModal('edit', false, this.state.current)
            }}
            onCancel={() => this.displayModal('edit', false, this.state.current)}
            visible={this.state.editVisible}
            enterpriseId={this.props.enterpriseId}
            contact={this.state.current === 1 ? this.props.contact : this.props.contact2}
            width="700px"
          />

          <PopModalClear
            title={'清空联系人信息'}
            handleSubmit={() => {
              this.props.actions.clearContactFor(this.state.current);
              this.displayModal('clear', false, this.state.current);
            }}
            onCancel={() => this.displayModal('clear', false, this.state.current)}
            visible={this.state.clearVisible}
            width="400px"
          />
        </div>
    )
  }
}

export default (connect(
  state => ({
    contacts: state.contact.contacts,
    currentId: state.contact.currentId,
    contact: state.contact.contact,
    contact2: state.contact.contact2,
    enterpriseId: state.auth.user ? state.auth.user.enterpriseId : null
  }),
  dispatch => ({actions: bindActionCreators(action, dispatch)})
)(cssModules(Contact, styles, {allowMultiple: true})))
