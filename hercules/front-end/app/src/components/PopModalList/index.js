import React, {Component} from 'react'
import {Table} from 'antd'
import {result} from 'lodash'

import Search from '../wrappedAntComponent/search'
import BasicModal from '../BasicModal'
import styles from './index.module.scss'

class PopModalList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      filteredInfo: {},
      currentId: null,
      selectedRowKeys: []
    }
  }

  componentWillReceiveProps = (nextProps) => {
    if (nextProps.data && nextProps.currentId === null) {
      this.setState({
        currentId: this.getDefaultContactId(nextProps.data),
        selectedRowKeys: this.getDefaultRowKey(nextProps.data)
      })
    }
    if (nextProps.currentId) {
      this.setState({
        currentId: nextProps.currentId,
        selectedRowKeys: this.getRowKey(nextProps.data, nextProps.currentId)
      })
    }
  };

  getDefaultContactId(contacts) {
    return result(contacts.filter(item => item.default), '[0]id')
  }

  getDefaultRowKey(data) {
    let defaultRow = 0;
    data.forEach((item, index) => {
      if (item.default) {
        defaultRow = index;
      }
    });
    return [defaultRow];
  }

  getRowKey(data, id) {
    let defaultRow = 0;
    data.forEach((item, index) => {
      if (item.id === id) {
        defaultRow = index;
      }
    });
    return [defaultRow];
  }

  onSelect = (record) => {
    this.setState({
      currentId: record.id,
    });
  };

  onSelectChange = (selectedRowKeys) => {
    this.setState({selectedRowKeys});
  };

  onSearch(value) {
    this.setState({
      filteredInfo: {
        name: [value]
      }
    })
  }

  render() {
    const {filteredInfo, currentId, selectedRowKeys} = this.state;
    const {data, handleSubmit, notice, onCancel} = this.props;

    const columns = [{
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      width: 100,
      filteredValue: filteredInfo.name || null,
      onFilter: (value, record) => record.name.includes(value),
    }, {
      title: 'Address',
      dataIndex: 'allAddress',
      key: 'allAddress',
    }, {
      title: 'Phone',
      dataIndex: 'phone',
      key: 'phone',
      width: 100,
    }];

    const rowSelection = {
      type: 'radio',
      selectedRowKeys,
      onSelect: this.onSelect,
      onChange: this.onSelectChange
    };

    return (
      <div>
        <BasicModal
          {...this.props}
          footer={[
            <button
              key="submitBtn"
              type="submit"
              className="button primary submit"
              onClick={() => handleSubmit(currentId)}>确定</button>,
            <button
              key="cancelBtn"
              type="button"
              className="button"
              onClick={onCancel}>取消</button>
          ]}
        >
          <div className={styles.modalList}>
            <div className="search-bar">
              <div className="notice">{notice && notice}</div>
              <Search
                defaultValue=""
                placeholder="输入联系人姓名"
                onSearch={::this.onSearch}
              />
            </div>
            <hr/>
            <Table
              rowKey={(record, index) => index}
              rowSelection={rowSelection}
              columns={columns}
              dataSource={data}
              pagination={{pageSize: 10}}
              showHeader={false}
              onRowClick={(record, index) => {
                this.onSelect(record)
                this.onSelectChange([index])
              }}
            />
          </div>
        </BasicModal>
      </div>
    );
  }
}

export default PopModalList;
