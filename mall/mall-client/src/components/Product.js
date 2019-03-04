import React from 'react';
import { Card, Col, Button, message } from 'antd';
import {ORDER_ID} from '../config/orderIdConfig';
const { Meta } = Card;

const Product = ({ id, name, price, unit, img, addOrderItemHandle }) => {
  return (
    <Col xs={{ span: 12 }} sm={{ span: 8 }} md={{ span: 6 }} lg={{span:4}}   style={{ padding: "5px" }}>
      <Card
        hoverable
        cover={<img alt={name} height="180" src={`./resources/img/${img}.jpg`} />}>
        <Meta title={name} description={`单价:${price}元/${unit}`} />
        <Button shape="circle" icon="plus" style={{ float: "right" }} onClick={() => {
          addOrderItemHandle(ORDER_ID, { productId: id, productCount: 1 },name,showNotice);
        }} />
      </Card>
    </Col>);
};

message.config({
  top: 20,
  duration: 1,
  maxCount: 1
});
const showNotice = (name) => {
  message.info(`${name}已经添加到订单！`);
}
export default Product;