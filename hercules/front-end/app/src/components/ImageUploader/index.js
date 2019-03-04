import React, {Component, PropTypes} from 'react'
import cssModules from 'react-css-modules'
import Upload from 'antd/lib/upload'
import Icon from 'antd/lib/icon'
import Input from 'antd/lib/input'
import httpClient from 'utils/http'
import styles from './index.module.scss'
import {BASE64_DEMO} from './base64Demo'

class ImageUploader extends Component {

  static propTypes = {
    demoImg: PropTypes.string.isRequired,
    form: PropTypes.object.isRequired,
    name: PropTypes.string.isRequired,
    isRequired: PropTypes.bool,
    imageFileName: PropTypes.string,
    callback: PropTypes.func
  }

  static defaultProps = {
    demoImg: '',
    form: null,
    name: '',
    isRequired: false
  }

  constructor(props) {
    super(props)
    this.state = {
      showError: false,
      isUploading: false,
      uploadFailed: false,
      imageUrl: ''
    }
    this.reset = ::this.reset
  }

  componentWillReceiveProps(nextProps) {
    const nextImg = nextProps.form.getFieldValue(this.props.name)
    if (nextImg && !this.state.imageUrl && !this.state.isUploading) {
      this.getUploadedImage(nextImg)
    }
  }

  getUploadedImage(nextImg) {
    httpClient.get(`/file/pictures/${nextImg}`, {
      responseType: 'arraybuffer'
    }).then((resp) => {
      this.getBase64FromBinary(resp.data, (imageUrl) => {
        this.setState({imageUrl})
      })
    })
      .catch(() => {
        this.setState({imageUrl: BASE64_DEMO})
      })
  }

  getBase64FromBinary(data, callback) {
    const blob = new Blob([data])
    const reader = new FileReader()
    reader.addEventListener('load', () => callback(reader.result))
    reader.readAsDataURL(blob)
  }


  getBase64(img, callback) {
    const reader = new FileReader()
    reader.addEventListener('load', () => callback(reader.result))
    reader.readAsDataURL(img)
  }

  beforeUpload(file) {
    this.setState({showError: false})
    this.setState({invalidFilename: false})
    const imageType = ['image/jpeg', 'image/png', 'image/bmp', 'image/jpg']
    const isLegalImg = imageType.indexOf(file.type) !== -1
    const isLt3M = file.size / 1024 / 1024 < 3
    const invalidFilename = /[%;]/.test(file.name)

    if (!isLegalImg || !isLt3M || invalidFilename) {
      this.setState({showError: true})
      if (invalidFilename) {
        this.setState({invalidFilename: true})
      }
    } else {
      this.setState({isUploading: true})
    }
    return isLegalImg && isLt3M && !invalidFilename
  }

  reset() {
    this.state = {
      showError: false,
      invalidFilename: false,
      uploadFailed: false,
      imageUrl: ''
    }
  }

  showErrorMessage() {
    let errorMessage = this.state.invalidFilename ? '文件名不能包含"%",";"等非法字符' : '照片不符合要求，请重新上传'
    errorMessage = this.state.uploadFailed ? '图片上传失败！' : errorMessage
    return errorMessage
  }

  render() {
    const {getFieldDecorator} = this.props.form
    return (
      <div className={styles.uploadContainer}>
        <p>Hercules平台对您所提供的证照信息将予以严格的保密管理，保证该证照信息仅用于Hercules平台的企业账号注册审核。</p>
        <section>
          <h3>上传照片仅支持jpg/jpeg/bmp/png的图片格式，图片文件大小小于3M。</h3>
          <Upload
            className={styles.uploader}
            name="filename"
            showUploadList={false}
            onChange={(info) => {
              if (info.file.error) {
                this.setState({showError: true, uploadFailed: true})
              }
            }}
            customRequest={(componentsData) => {
              const originFile = componentsData.file
              const formData = new FormData()
              formData.append(componentsData.filename, originFile, originFile.name)
              httpClient.post('/file/pictures', formData).then((data) => {
                this.getBase64(originFile, imageUrl => {
                  this.setState({ imageUrl }, () => {
                    const fieldValue = {}
                    fieldValue[this.props.name] = data.data
                    this.props.form.setFieldsValue(fieldValue)
                    this.setState({isUploading: false})
                    componentsData.onSuccess()
                  })
                })
                if (this.props.callback) {
                  this.props.callback()
                }
              })
            }}
            beforeUpload={::this.beforeUpload}>
            {
              this.state.imageUrl ? (
                <div className="image-container">
                  <img src={this.state.imageUrl} alt="" className="avatar"/>
                  {this.state.isUploading && <span className="uploading-icon"/>}
                </div>)
                : <Icon type="plus" className="avatar-uploader-trigger"/>
            }
            <h3 className="uploadTips">请上传附件照片</h3>
            {getFieldDecorator(this.props.name, {
              rules: [{
                required: this.props.isRequired, message: '请上传图片'
              }]
            })(
              <Input className="hide"/>
            )}
            {this.state.showError && <h3 className="uploadTips error">
              {this.showErrorMessage()}
              </h3>}
          </Upload>
        </section>
        <section>
          <h3>示例</h3>
          <div>
            <img src={this.props.demoImg}/>
            <span>要求：文字清晰易识别</span>
          </div>
        </section>
      </div>
    )
  }
}

export default cssModules(ImageUploader, styles)
