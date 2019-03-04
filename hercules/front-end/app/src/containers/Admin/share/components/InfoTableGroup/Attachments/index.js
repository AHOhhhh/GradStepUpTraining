import React from 'react'
import cssModules from 'react-css-modules'
import ImagePreviewer from 'components/ImagePreviewer'

import styles from './index.module.scss'

class Attachments extends React.Component {

  state = {
    visible: false,
    currentFilename: ''
  }

  handleClick(filename) {
    this.setState({
      visible: true,
      currentFilename: filename
    })
  }

  closeModal() {
    this.setState({
      visible: false,
      imageData: null
    })
  }

  render() {
    const {enterpriseInfo} = this.props
    const files = [
      {name: '统一社会信用代码证书', hashedName: enterpriseInfo.certificateForUniformSocialCreditCode},
      {name: '营业执照编号', hashedName: enterpriseInfo.certificateForBusinessLicense},
      {name: '纳税人识别号', hashedName: enterpriseInfo.certificateForTaxRegistration},
      {name: '组织机构代码', hashedName: enterpriseInfo.certificateForOrganization}
    ].filter(file => {
      return file.hashedName
    })

    return (
      <div styleName="attachments">
        <div styleName="division"/>
        <div styleName="item"><span styleName="item-label">附件：</span>
          {files.map(file => {
            return (
              <span styleName="attachment" key={file.name}>
                <span styleName="attachment-icon"/><span styleName="file-name"><a
                  onClick={this.handleClick.bind(this, file.hashedName)}>{file.name}</a></span>
              </span>
            )
          })}
          <ImagePreviewer
            visible={this.state.visible}
            onCancel={this.closeModal.bind(this)}
            filename={this.state.currentFilename}
          />
        </div>
      </div>
    )
  }

}

export default cssModules(Attachments, styles)