import React from 'react';
import {
TabContent, TabPane
} from 'reactstrap';
import ShopItems from './ShopItems';
import ShopCart from './ShopCart';
const TabContainer = ({ activeTab }) => {
  return (
    <TabContent activeTab={activeTab}>
      <TabPane tabId="1" className="TabPane">
        <ShopItems />
      </TabPane>
      <TabPane tabId="2" className="TabPane">
        <ShopCart />
      </TabPane>
    </TabContent>);
}
export default TabContainer;