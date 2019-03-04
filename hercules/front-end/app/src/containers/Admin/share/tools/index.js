import {Modal} from 'antd';

export const renderModifyStatusError = () => {
  Modal.error({
    title: '企业账号(启用/停用)状态修改失败！'
  });
}