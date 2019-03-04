import React from 'react'

export default class TableOperation extends React.Component {
  editItem(e) {
    const {showProductModal, record} = this.props
    showProductModal(record, e)
  }

  deleteItem(e) {
    const {showDeleteConfirmModal, record} = this.props
    showDeleteConfirmModal(record, e)
  }

  render() {
    return (
      <span className="actions">
        <button className="table-operate" onClick={::this.editItem}>编辑</button>
        <button className="table-operate" onClick={::this.deleteItem}>删除</button>
      </span>
    )
  }
}
