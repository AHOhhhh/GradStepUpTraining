import { notification } from 'antd';

const errorNotification = (message, description) => {
  notification.error({
    message,
    description
  });
}
export default errorNotification;
