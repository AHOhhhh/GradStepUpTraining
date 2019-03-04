import React, { Component } from 'react'
import { Button, Upload, message, Modal } from 'antd/lib'
import finishedSVG from 'containers/shared/assets/order-finished.svg'
import failedSVG from 'containers/shared/assets/failed.svg'
import cssModule from 'react-css-modules'
import { isEmpty } from 'lodash'
import styles from './index.module.scss'


class OrderBillUploader extends Component {
  state = {
    modalInfo: {
      visible: false
    }
  }

  generateModalInfo({ data }) {
    const {
      count,
      duplicatedOrderIds,
      nonExistOrderIds,
      statusErrorOrderIds,
      status
    } = data
    const isSuccessfull = status === 'success'
    let title
    const messages = []
    let icon

    if (isSuccessfull) {
      title = '导入成功'
      icon = finishedSVG
      messages.push(`已导入 "${count}" 条数据`)
    } else {
      title = '导入失败'
      icon = failedSVG

      if (!isEmpty(duplicatedOrderIds)) {
        messages.push(`订单号重复："${duplicatedOrderIds.join(',')}"`)
      }

      if (!isEmpty(nonExistOrderIds)) {
        messages.push(`未查询到订单："${nonExistOrderIds.join(',')}"`)
      }

      if (!isEmpty(statusErrorOrderIds)) {
        messages.push(`对账单状态不合法："${statusErrorOrderIds.join(',')}"`)
      }
    }
    return {
      visible: true,
      title,
      btnName: '关闭',
      messages,
      inprogress: false,
      icon
    }

  }

  customRequest(file) {
    this.setState({
      modalInfo: {
        visible: true,
        title: '正在导入，请稍等......',
        btnName: '取消',
        inprogress: true
      }
    }, () => {
      this.props.action(file)
        .then(response => {
          this.setState({
            modalInfo: this.generateModalInfo(response)
          })
        })
        .catch((error) => {
          let message = '服务异常！';
          const errorInfo = error.data
          if (error.status === 413 && errorInfo.code === 17000) {
            message = `您导入的数据已超出系统上限（${errorInfo.details.permitted}条）请检查后重新导入`
          }
          this.setState({
            modalInfo: {
              visible: true,
              title: '导入失败',
              btnName: '关闭',
              messages: [message],
              inprogress: false,
              icon: failedSVG
            }
          })
        })
    })
  }

  beforeUpload(file) {
    const { size, name } = file
    const isExcel = /\.xlsx$/.test(name)
    const isValidSize = size / 1024 / 1024 < 10
    const isValid = isExcel && isValidSize
    if (!isValid) {
      message.info('导入失败！导入文件格式错误！(大小不超过10M)')
    }
    return isValid
  }

  close() {
    this.setState({ modalInfo: { visible: false } })
    window.location.reload()
  }

  render() {
    const { title, messages = [], btnName, visible, inprogress, icon } = this.state.modalInfo
    return (
      <div className={styles.upload}>
        <Upload
          showUploadList={false}
          beforeUpload={::this.beforeUpload}
          customRequest={::this.customRequest}
          name="fileUpload"
        >
          <Button className={styles.btn}>
            导入
          </Button>
        </Upload>
        <Modal
          visible={visible}
          maskClosable={false}
          footer={null}
          width={520}
          closable={false}

        >
          <div className={styles.modalContainer}>
            <div className={styles.title}>
              <img src={icon} />
              <span>{title}</span>
            </div>
            <div className={styles.messageContainer}>
              {messages.map(message => (
                <div key={message} className={styles.message}>
                  <p>{message}</p>
                </div>)
              )}
            </div>
            <Button
              className={styles.close}
              onClick={::this.close}
              disabled={inprogress}
            >
              {btnName}
            </Button>
          </div>
        </Modal>
      </div>
    )
  }
}

export default cssModule(OrderBillUploader, styles)