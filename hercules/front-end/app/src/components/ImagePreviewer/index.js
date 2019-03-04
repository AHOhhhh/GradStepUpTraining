import React, {Component, PropTypes} from 'react'; // eslint-disable-line
import httpClient from 'utils/http'

import {Icon} from 'antd'
import cssModules from 'react-css-modules';
import styles from './index.module.scss';
import {BASE64_DEMO} from '../ImageUploader/base64Demo';

class ImagePreviewer extends Component { // eslint-disable-line
  
  state = {
    imageData: null,
    loading: false
  }
  
  componentWillReceiveProps(nextProps) {
    const {visible, filename} = nextProps
    if (visible) {
      window.scrollTo(0, 0);
      document.body.style.overflow = 'hidden'
    } else {
      document.body.style.overflow = 'auto'
    }
    
    if (filename !== this.props.filename) {
      this.getImageDataByFilename(filename)
    }
  }
  
  getImageDataByFilename = filename => {
    this.setState({loading: true})
    httpClient.get('/file/pictures/' + filename, {
      responseType: 'arraybuffer'
    }).then(resp => {
      this.setState({
        imageData: this.convertToBase64ImageData(resp.data),
        loading: false
      })
    }).catch(() => {
      this.setState({
        imageData: BASE64_DEMO,
        loading: false
      })
    })
  }

  convertToBase64ImageData = (respData) => {
    return 'data:image/png;base64,' + new Buffer(respData, 'binary').toString('base64')
  }
  
  render() {
    const className = this.props.visible ? styles.previewMask : styles.hidden;
    const imageData = this.state.imageData
    return (
      <div className={className}>
        <div className={styles.previewCancelIcon} onClick={this.props.onCancel} />
        {this.state.loading ? <Icon type="loading" style={{color: '#fff'}} /> :
        <img src={imageData} className={styles.previewImage}/>}
      </div>
    );
  }
}

ImagePreviewer.propTypes = {
  imageData: PropTypes.string,
  visible: PropTypes.bool,
  onCancel: PropTypes.func
};

export default cssModules(ImagePreviewer, styles);
