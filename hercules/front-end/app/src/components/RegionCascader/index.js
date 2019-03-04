import React from 'react';
import { Cascader } from 'antd';
import options from './region_data'
// import styles from './index.module.scss';

const RegionCascade = (props) => (
  <div>
    <Cascader
      {...props}
      options={options}/>
  </div>
)

export default RegionCascade
