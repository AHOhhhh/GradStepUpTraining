import React, { Component } from 'react';
import {
  Container
} from 'reactstrap';
import NavContainer from './components/NavContainer';
import TabContainer from './components/TabContainer';

import './App.css'
class App extends Component {
  constructor(props) {
    super(props);
    let url = props.match.url;
    let tab = url === '/' || url === '/market' ? '1' : '2'
    this.state = {
      activeTab: tab
    };
  }
  handleToggle = (tab) => {
    if (this.state.activeTab !== tab) {
      this.setState({
        activeTab: tab,
      });
    }
  }
  render() {
    return (
      <Container className="Container">
        <NavContainer activeTab={this.state.activeTab} handleToggle={this.handleToggle} />
        <TabContainer activeTab={this.state.activeTab} />
      </Container>
    );
  }
}
export default App;