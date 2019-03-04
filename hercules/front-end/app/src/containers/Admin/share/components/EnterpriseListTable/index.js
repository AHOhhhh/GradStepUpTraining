import React from 'react'
import {Table} from 'antd'

const EnterpriseListTable = ({total, dataSource, columns, renderActionColumn, onPageParamChange, getRowClassName}) => {
  const realColumns = columns.concat({
    title: '操作',
    key: 'action',
    render: renderActionColumn
  })

  return (
    <div className="list">
      <Table
        dataSource={dataSource}
        columns={realColumns}
        pagination={{
          total,
          showSizeChanger: true,
          onChange: (page, size) => { onPageParamChange(page, size) },
          onShowSizeChange: (page, size) => { onPageParamChange(page, size) }
        }}
        rowKey={record => record.id}
        rowClassName={getRowClassName}
      />
    </div>
  )
}

export default EnterpriseListTable