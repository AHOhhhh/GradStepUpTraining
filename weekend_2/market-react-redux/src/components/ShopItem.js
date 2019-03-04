import React from 'react';
import { Card, CardImg, CardText, CardBody, CardTitle, Button, Col } from 'reactstrap';

const ShopItem = ({ barcode, name, unit, price, handleAddItem }) => {
  return (
    <Col sm="4">
      <Card>
        <CardImg top width="100%" src={`./resources/images/${barcode}.jpg`} alt="Card image cap" />
        <CardBody>
          <CardTitle >{name}</CardTitle>
          <CardText><strong>单价</strong><span className="fa fa-dollar-sign"></span>：{price} 元/{unit}</CardText>
          <Button outline className="button" color="secondary" size="sm" onClick={() => { console.log(`${name}已添加到购物车`)
            handleAddItem({ barcode, name, price, count: 1 }) }}><span className="fa fa-cart-plus"></span></Button>
        </CardBody>
      </Card>
    </Col>
  );
}

export default ShopItem;