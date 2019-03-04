import React, {Component} from 'react' // eslint-disable-line
import {Input} from 'antd';
import {Link} from 'react-router'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import cssModules from 'react-css-modules'
import * as action from 'actions'
import EnterpriseListTable from '../../share/components/EnterpriseListTable'
import styles from './index.module.scss'

const Search = Input.Search;

const columns = [
  {
    title: '企业名称',
    dataIndex: 'name',
    key: 'name',
    styleName: 'max-width-140',
    width: 300,
  }, {
    title: '企业管理员',
    dataIndex: 'admin.fullname',
    key: 'admin.fullname'
  }, {
    title: '管理员联系方式',
    key: 'adminContact',
    render: (text, record) => (
      <span>
        {record.admin.cellphone ? record.admin.cellphone : ''}
        {record.admin.cellphone ? <br/> : ''}
        {record.admin.telephone}
      </span>
    )
  }, {
    title: '法人',
    dataIndex: 'artificialPersonName',
    key: 'artificialPersonName'
  }, {
    title: '法人联系电话',
    dataIndex: 'artificialPersonContact',
    key: 'artificialPersonContact'
  }
]

class AuthorizationInProcess extends Component { // eslint-disable-line

  state = {
    pageSize: 10,
    pageNum: 1,
    enterpriseName: null
  }

  componentDidMount() {
    this.getEnterprises({name: null})
  }

  getEnterprises() {

    const {pageNum, pageSize, enterpriseName} = this.state

    this.props.actions.adminGetEnterprisesInProcess({
      name: enterpriseName,
      validationStatus: 'AuthorizationInProcess',
      page: pageNum - 1,
      size: pageSize
    })
  }

  search(name) {
    this.setState({enterpriseName: name, pageNum: 1}, () => this.getEnterprises())
  }

  handlePageParamChange(pageNum, pageSize) {
    this.setState({
      pageNum,
      pageSize
    }, () => {
      this.getEnterprises()
    })
  }

  renderActionColumn(text, record) {
    return (
      <span>
        <Link to={'/admin/enterprises/' + record.id} className="red-button">审核</Link>
      </span>
    )
  }


  render() {
    const enterprises = this.props.admin.enterpriseInProgress

    const dataSource = enterprises ? enterprises.content : []
    const total = enterprises.totalElements || 0;

    return (
      <div styleName="main-panel">
        <div styleName="header-area">
          <h2 styleName="title">
            <span styleName="main">待审核企业</span>（
            <span styleName="count"> {total}</span>家 ）
          </h2>
          <Search
            styleName="search-box"
            placeholder="输入企业名称进行查找"
            onSearch={value => this.search(value.trim())}
          />
          <div styleName="clear-line"/>
        </div>

        <EnterpriseListTable
          dataSource={dataSource}
          columns={columns}
          total={total}
          onPageParamChange={::this.handlePageParamChange}
          renderActionColumn={::this.renderActionColumn}
        />
      </div>
    )
  }
}

export default connect(
  state => ({
    admin: state.admin,
  }),
  dispatch => ({actions: bindActionCreators(action, dispatch)})
)(cssModules(AuthorizationInProcess, styles, {allowMultiple: true}))

