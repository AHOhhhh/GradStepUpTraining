import React, { Component, PropTypes } from 'react'
import { bindActionCreators } from 'redux'
import cssModules from 'react-css-modules'
import { connect } from 'react-redux'
import { Row, Col, Icon, Table, Modal } from 'antd/lib'
import Search from 'components/wrappedAntComponent/search'

import { PopModalEdit } from 'components'
import * as action from 'actions'
import LeftNavigation from 'components/LeftNavigation'
import ModalFooterButtons from '../../shared/ModalFooterButtons'

import styles from './index.module.scss';

@connect(
  state => ({
    user: state.auth.user,
    contact: state.contact.contact,
    contacts: state.contact.contacts,
    enterpriseId: state.auth.user ? state.auth.user.enterpriseId : null
  }),
  dispatch => ({ actions: bindActionCreators(action, dispatch) })
)
class ContactInfoContainer extends Component {
  constructor(props) {
    super(props);
    this.state = {
      filteredInfo: {},
      addVisible: false,
      editVisible: false,
      deleteVisible: false,
      pageSize: 10
    }
  }

  static propTypes = {
    contacts: PropTypes.array,
  }

  static defaultProps = {
    contacts: []
  }

  componentDidMount() {
    this.props.actions.getContacts(this.props.enterpriseId)
  }

  onSearch(value) {
    this.setState({
      filteredInfo: {
        name: [value]
      }
    })
  }

  onEdit(record) {
    this.props.actions.selectContact(record.id)
    this.setState({ editVisible: true })
  }

  onDelete(record) {
    this.props.actions.selectContact(record.id)
    this.setState({ deleteVisible: true })
  }

  confirmDelete() {
    const { contact, enterpriseId } = this.props
    this.props.actions.deleteContact(contact.id, enterpriseId, () => {
      this.closeModal()
    })
  }

  closeModal() {
    this.setState({
      addVisible: false,
      editVisible: false,
      deleteVisible: false
    })
  }

  changePageSize(pageNum, pageSize) {
    this.setState({pageSize})
  }

  render() {
    const { filteredInfo } = this.state;
    const { contacts } = this.props;
    const columns = [{
      title: '联系人',
      dataIndex: 'name',
      key: 'name',
      filteredValue: filteredInfo.name || null,
      onFilter: (value, record) => record.name.includes(value),
    }, {
      title: '详细地址',
      dataIndex: 'allAddress',
      key: 'allAddress',
    }, {
      title: '联系方式',
      dataIndex: 'phone',
      key: 'phone',
    }, {
      title: '操作',
      dataIndex: 'operation',
      key: 'operation',
      render: (text, record) => (
        <div>
          <a className="red-button table-operation" onClick={() => this.onEdit(record)}>编辑</a>
          <a className="red-button" onClick={() => this.onDelete(record)}>删除</a>
        </div>
      )
    }];

    return (
      <div className="user-container">
        <Row>
          <Col span={4}>
            <LeftNavigation
              location={this.props.location.pathname}/>
          </Col>
          <Col span={20}>
            <div className={styles.contactContainer}>
              <h2>联系方式管理</h2>

              <div className="operation">
                <div className="search-bar">
                  <Search
                    defaultValue=""
                    placeholder="按姓名查找"
                    onSearch={::this.onSearch}
                  />
                </div>
                <div className="add-contact" onClick={() => this.setState({addVisible: true})}>
                  <Icon type="plus"/>
                  <span>添加联系方式</span>
                </div>
              </div>

              <Table
                rowKey={(record, index) => index}
                columns={columns}
                dataSource={contacts}
                pagination={{ showSizeChanger: true, pageSize: this.state.pageSize, onShowSizeChange: ::this.changePageSize }}
              />

              {this.state.addVisible && (<PopModalEdit
                title={'新增联系方式'}
                handleSubmit={(params) => {
                  this.props.actions.createContactDual(params, 1, this.props.enterpriseId);
                  this.closeModal();
                }}
                onCancel={() => this.closeModal()}
                visible={this.state.addVisible}
                enterpriseId={this.props.enterpriseId}
                page={this.props.page}
                width="700px"
              />)}

              <PopModalEdit
                title={'编辑联系方式'}
                handleSubmit={(params) => {
                  const id = this.props.contact.id;
                  this.props.actions.updateContact(id, params, this.props.enterpriseId);
                  this.closeModal()
                }}
                onCancel={() => this.closeModal()}
                visible={this.state.editVisible}
                enterpriseId={this.props.enterpriseId}
                page={this.props.page}
                contact={this.props.contact}
                width="700px"
              />

              <Modal
                title="删除联系人"
                visible={this.state.deleteVisible}
                wrapClassName={styles.deleteContactContainer}
                onCancel={::this.closeModal}
                footer={<ModalFooterButtons
                  okText="确定" cancelText="取消" onOk={::this.confirmDelete} onCancel={::this.closeModal}/>}
              >
                <p className="confirm-text">确定要删除该联系人?</p>
              </Modal>
            </div>
          </Col>
        </Row>
      </div>
    )
  }
}

export default cssModules(ContactInfoContainer, styles, { allowMultiple: true })
