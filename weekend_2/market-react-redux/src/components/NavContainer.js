import React from 'react';
import {
  Nav, NavItem, NavLink
} from 'reactstrap';
import { NavLink as NavLinkRouter } from 'react-router-dom';
const NavContainer = ({ activeTab, handleToggle }) => {
  return (
    <Nav tabs>
      <NavLinkRouter to="/market">
        <NavContainerItem tabIdx='1' tabName='商城' activeTab={activeTab} handleToggle={handleToggle} />
      </NavLinkRouter>

      <NavLinkRouter to="/cart">
        <NavContainerItem tabIdx='2' tabName='购物车' activeTab={activeTab} handleToggle={handleToggle} />
      </NavLinkRouter>
    </Nav>);
}

const NavContainerItem = ({ tabIdx, tabName, activeTab, handleToggle }) => {
  return (<NavItem>
    <NavLink
      className={activeTab === tabIdx ? 'active' : ''}
      onClick={() => { handleToggle(tabIdx); }}>
      {tabName}
    </NavLink>
  </NavItem>);
}

export default NavContainer;